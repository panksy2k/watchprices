package com.affiliation.product;

import com.affiliation.product.di.ApplicationModule;
import com.affiliation.product.web.Auth;
import com.affiliation.product.web.ProductController;
import com.affiliation.product.web.WatchFeaturesController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  private ProductController productController;
  private Auth authController;
  private WatchFeaturesController watchFeaturesController;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    MainVerticle verticle = new MainVerticle();
    vertx.deployVerticle(verticle)
      .compose(id -> {
        //watchFeaturesController.putWatchFeatures();
        return Future.succeededFuture(true);
      })
      .onFailure(err -> logger.error(err.getMessage()))
      .onSuccess(result -> {
        logger.info("Verticle deployed with success?: {}", result);
      });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // Use environment variables for MongoDB connection, fallback to localhost for development
    String mongoHost = System.getProperty("MONGO_HOST", "localhost");
    String mongoPort = System.getProperty("MONGO_PORT", "27017");
    String mongoDatabase = System.getProperty("MONGO_DATABASE", "productdb");
    String mongoConnectionString = System.getProperty(
      "MONGO_CONNECTION_STRING",
      "mongodb://" + mongoHost + ":" + mongoPort
    );

    String serverKeyStore = System.getProperty("SERVER_KEYSTORE_PATH",
      "/Users/pankajpardasani/Documents/watchprices-certificates/server.p12");
    String serverKeyStoreSecret = System.getProperty("SERVER_KEYSTORE_SECRET", "secret");

    JsonObject mongoConfig = new JsonObject()
      .put("connection_string", mongoConnectionString)
      .put("db_name", mongoDatabase);

    logger.info("Connecting to MongoDB at: {}, database: {}", mongoConnectionString, mongoDatabase);

    logger.info("Initializing application module with MongoDB configuration");
    ApplicationModule appModule = new ApplicationModule(vertx, mongoConfig);
    Injector injector = Guice.createInjector(appModule);

    // Inject ProductController
    productController = injector.getInstance(ProductController.class);
    logger.info("ProductController initialized successfully");

    // Inject AuthController
    authController = injector.getInstance(Auth.class);
    logger.info("AuthController initialized successfully");

    // Inject WatchFeaturesController
    watchFeaturesController = injector.getInstance(WatchFeaturesController.class);
    logger.info("WatchFeaturesController initialized successfully");

    Router router = Router.router(vertx);

    // Enable CORS for Vue.js integration
    router.route().handler(CorsHandler.create("*")
      .allowedMethod(io.vertx.core.http.HttpMethod.GET)
      .allowedMethod(io.vertx.core.http.HttpMethod.POST)
      .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
      .allowedMethod(io.vertx.core.http.HttpMethod.DELETE)
      .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
      .allowedHeader("Content-Type")
      .allowedHeader("Authorization"));

    router.route().handler(BodyHandler.create());

    // API routes
    setupProductRoutes(router);
    logger.info("API routes configured");

    // Serve static files (Vue.js app)
    router.route("/*").handler(StaticHandler.create("frontend/dist"));

    // Redirect all non-API traffic to the Vue app's entry point
    router.get("/*").handler(ctx -> {
      if (!ctx.request().path().startsWith("/api")) {
        ctx.response().sendFile("frontend/dist/index.html");
      }
    });
    logger.info("Static file handler configured for webroot");

    HttpServerOptions options = new HttpServerOptions()
      .setSsl(true)
      .setPfxKeyCertOptions(new PfxOptions()
        .setPath(serverKeyStore)
        .setPassword(serverKeyStoreSecret))
      .setPort(443)
      .setHost("0.0.0.0");

    vertx.createHttpServer(options)
      .requestHandler(router)
      .listen(ar -> {
        if (ar.succeeded()) {
          logger.info("HTTPS server started on port {}", options.getPort());
          logger.info("Vue.js app available at: https://localhost:" + options.getPort());

          // Initialize watch features data after MongoDB is ready
          watchFeaturesController.putWatchFeatures()
            .onSuccess(result -> logger.info("WatchFeaturesController injected static data successfully"))
            .onFailure(err -> logger.error("Failed to inject watch features data: {}", err.getMessage()));

          startPromise.complete();
        } else {
          startPromise.fail(ar.cause());
          logger.error("Failed to start HTTPS server", ar.cause());
        }
      });
  }

  private void setupProductRoutes(Router router) {
    // Create new product
    router.post("/api/secured/products").handler(ctx -> {
      logger.info("POST /api/products - Creating product: {}", ctx.body().asString());
      productController.createProduct(ctx)
        .onSuccess(result -> {
          JsonObject response = new JsonObject()
            .put("success", true)
            .put("data", result);
          ctx.response()
            .setStatusCode(201)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        })
        .onFailure(error -> {
          logger.error("Error creating product {}", error.getMessage());
          JsonObject response = new JsonObject()
            .put("success", false)
            .put("message", error.getMessage());
          ctx.response()
            .setStatusCode(400)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    router.get("/api/products/:productType").handler(ctx -> {
      logger.info("GET /api/products/{} - Processing request", ctx.pathParam("productType"));

      productController.findAllProducts(ctx)
        .onSuccess(productList -> {
          JsonObject response = JsonObject.mapFrom(productList);
          HttpServerResponse httpServerResponse =
            ctx.response().setStatusCode(200).putHeader("Content-Type", "application/json");

          if (productList.getPage() > 0 && productList.getTotalPages() > 0) {
            httpServerResponse.putHeader("X-APP-PAGINATION", "true")
              .putHeader("X-APP-PAGINATION-TOTAL-PAGES", Integer.toString(productList.getTotalPages()))
              .putHeader("X-APP-PAGINATION-TOTAL-ENTITIES", Integer.toString(productList.getTotalSize()))
              .putHeader("X-APP-PAGINATION-CURRENT-PAGE", Integer.toString(productList.getPage()));
          }

          httpServerResponse.end(response.encode());
        })
        .onFailure(error -> {
          logger.error("Error fetching product {}", error.getMessage());
          JsonObject response = new JsonObject()
            .put("success", false)
            .put("message", error.getMessage());
          ctx.response()
            .setStatusCode(400)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    // Get single product by ID
    router.get("/api/products/:productType/:id").handler(ctx -> {
      logger.info("GET /api/products/{}/{} - Fetching product", ctx.pathParam("productType"), ctx.pathParam("id"));
      productController.findProductById(ctx)
        .onSuccess(product -> {
          ctx.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(product.encode());
        })
        .onFailure(error -> {
          logger.error("Error fetching product by ID {}: {}", ctx.pathParam("id"), error.getMessage());
          JsonObject response = new JsonObject()
            .put("success", false)
            .put("message", error.getMessage());
          ctx.response()
            .setStatusCode(404)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    router.put("/api/secured/products/:productType/:id").handler(ctx -> {
      logger.info("PUT /api/products/{}/{} - Updating product", ctx.pathParam("productType"), ctx.pathParam("id"));

      productController.updateProduct(ctx).onSuccess(productId -> {
          JsonObject response = JsonObject.of("id", productId);
          ctx.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        })
        .onFailure(error -> {
          logger.error("Error updating product {}", error.getMessage());
          JsonObject response = new JsonObject()
            .put("success", false)
            .put("message", error.getMessage());
          ctx.response()
            .setStatusCode(400)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    router.get("/api/products/:productType/attribute/:attributeName").handler(ctx -> {
      logger.info("GET /api/products/{}/attribute/{} - Fetching product attributes",
        ctx.pathParam("productType"), ctx.pathParam("attributeName"));

      productController.findProductAttribute(ctx).onSuccess(productAttributeList -> {
        JsonObject response = new JsonObject();
        String attributeName = ctx.pathParam("attributeName");
        response.put(attributeName, productAttributeList);
        ctx.response().setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(response.encode());
      }).onFailure(error -> {
        logger.error("Error fetching product attribute - {}: {}", ctx.pathParam("attributeName"), error.getMessage());

        JsonObject response = new JsonObject()
          .put("success", false)
          .put("message", error.getMessage());

        ctx.response()
          .setStatusCode(400)
          .putHeader("Content-Type", "application/json")
          .end(response.encode());
      });
    });

    router.post("/api/products/:productType/find/criteria").handler(ctx -> {
      logger.info("POST /api/products/{}/find/criteria - Request body: {}", ctx.pathParam("productType"),
        ctx.body().asString());

      productController.findProductByCriteria(ctx)
        .onSuccess(productList -> {
          logger.info("Fetched: {}", productList.toString());

          JsonObject response = new JsonObject()
            .put("success", true)
            .put("data", productList);

          ctx.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        })
        .onFailure(error -> {
          logger.error("Error fetching product {}", error.getMessage());
          JsonObject response = new JsonObject()
            .put("success", false)
            .put("message", error.getMessage());

          ctx.response()
            .setStatusCode(400)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    router.post("/api/auth/signup").handler(ctx -> {
      JsonObject bodyObject = ctx.body().asJsonObject();
      String email = bodyObject.getString("email").trim().toLowerCase();
      String password = bodyObject.getString("password");

      authController.signup(email, password)
        .onFailure(h -> ctx.fail(h))
        .onSuccess(h -> {
          JsonObject response = new JsonObject();
          response.put("success", true);

          ctx.response().setStatusCode(201).end(response.encode());
        });
    });

    router.post("/api/auth/login").handler(ctx -> {
      JsonObject bodyObject = ctx.body().asJsonObject();
      String email = bodyObject.getString("email").trim().toLowerCase();
      String password = bodyObject.getString("password");

      authController.login(email, password)
        .onFailure(h -> {
          logger.info("Login failed for {}", email);
          ctx.fail(h);
        })
        .onSuccess(h -> {
          JsonObject response = new JsonObject();
          response.put("token", h);

          logger.info("Login successful by {}", email);
          ctx.response().setStatusCode(200).end(response.encode());
        });
    });

    router.route("/api/secured/*").handler(authController::authenticate);

    router.get("/api/products/:productType/watchFeatures/:watchFeatures").handler(ctx -> {
      String productType = ctx.pathParam("productType");
      String featureName = ctx.pathParam("watchFeatures");

      logger.info("GET /api/secured/products/{}/{} - Fetching watch feature type static data ",
        productType, featureName);

      watchFeaturesController.getFeature(productType, featureName)
        .onFailure(t -> {
          logger.error("Could not fetch watch feature type {}", featureName);
          ctx.fail(t);
        })
        .onSuccess(feature -> {
          JsonObject response = new JsonObject();
          response.put(feature.getFeatureName(), feature.getFeatures());

          ctx.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
        });
    });

    logger.info("Product API routes configured");
  }
}
