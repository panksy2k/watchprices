package com.affiliation.product.di;

import com.affiliation.product.repository.IProductRepository;
import com.affiliation.product.repository.IWatchFeatures;
import com.affiliation.product.repository.JsonTSportsWatchConverter;
import com.affiliation.product.repository.JsonToUserConverter;
import com.affiliation.product.repository.Product;
import com.affiliation.product.repository.ProductRepository;
import com.affiliation.product.repository.User;
import com.affiliation.product.repository.UserRepository;
import com.affiliation.product.repository.WatchFeaturesRepository;
import com.affiliation.product.web.Auth;
import com.affiliation.product.web.AuthController;
import com.affiliation.product.web.IToken;
import com.affiliation.product.web.ProductController;
import com.affiliation.product.web.ProductPricePublisher;
import com.affiliation.product.web.TokenService;
import com.affiliation.product.web.WatchFeaturesController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guice module for dependency injection configuration
 */
public class ApplicationModule extends AbstractModule {
  private static final Logger logger = LoggerFactory.getLogger(ApplicationModule.class);

  private static final String PRODUCT_SPORTS_WATCH_COLLECTION = "SPORTSWATCH";
  private static final String USER_COLLECTION = "USER";
  private static final String WATCH_FEATURES = "WatchFeatures";

  private final Vertx vertx;
  private final JsonObject mongoConfig;

  public ApplicationModule(Vertx vertx, JsonObject mongoConfig) {
    this.vertx = vertx;
    this.mongoConfig = mongoConfig;
  }

  @Override
  protected void configure() {
    // Bind interfaces to implementations
    bind(IProductRepository.class).to(ProductRepository.class);
  }

  @Provides
  @Singleton
  public MongoClient provideMongoClient() {
    // Create MongoDB client with configuration
    // Default configuration if none provided
    JsonObject config = mongoConfig != null ? mongoConfig :
      new JsonObject()
        .put("connection_string", "mongodb://localhost:27017")
        .put("db_name", "productdb");

    MongoClient mc = MongoClient.createShared(vertx, config);
    mc.createCollection(PRODUCT_SPORTS_WATCH_COLLECTION, h -> {
      if (h.succeeded()) {
        System.out.println("Created collection " + PRODUCT_SPORTS_WATCH_COLLECTION);
      } else {
        System.out.println("Failed to create collection " + PRODUCT_SPORTS_WATCH_COLLECTION);
        h.cause().printStackTrace();
      }
    });

    mc.createCollection(USER_COLLECTION, h -> {
      if (h.succeeded()) {
        System.out.println("Created collection " + USER_COLLECTION);
      } else {
        System.out.println("Failed to create collection " + USER_COLLECTION);
        h.cause().printStackTrace();
      }
    });

    JsonObject batteryLifeDailyUse = new JsonObject().put("productType", 1).put("batteryLifeDailyUse", 1);
    JsonObject screenMaterial = new JsonObject().put("productType", 1).put("screenMaterial", 1);
    JsonObject internalMemory = new JsonObject().put("productType", 1).put("internalMemory", 1);
    JsonObject chargingTime = new JsonObject().put("productType", 1).put("chargingTime", 1);
    JsonObject waterResistance = new JsonObject().put("productType", 1).put("waterResistance", 1);

    mc.createIndex(PRODUCT_SPORTS_WATCH_COLLECTION, batteryLifeDailyUse, h -> {
      if (h.succeeded()) {
        logger.info("Created index on batteryLifeDailyUse (and productType)");
      }
    });

    mc.createIndex(PRODUCT_SPORTS_WATCH_COLLECTION, screenMaterial, h -> {
      if (h.succeeded()) {
        logger.info("Created index on screenMaterial (and productType)");
      }
    });

    mc.createIndex(PRODUCT_SPORTS_WATCH_COLLECTION, internalMemory, h -> {
      if (h.succeeded()) {
        logger.info("Created index on internalMemory (and productType)");
      }
    });

    mc.createIndex(PRODUCT_SPORTS_WATCH_COLLECTION, chargingTime, h -> {
      if (h.succeeded()) {
        logger.info("Created index on chargingTime (and productType)");
      }
    });

    mc.createIndex(PRODUCT_SPORTS_WATCH_COLLECTION, waterResistance, h -> {
      if (h.succeeded()) {
        logger.info("Created index on waterResistance (and productType)");
      }
    });

    return mc;
  }

  @Provides
  @Singleton
  public ProductRepository<Product> provideProductRepository(MongoClient mongoClient) {
    return new ProductRepository<>(mongoClient);
  }

  @Provides
  @Singleton
  public ProductController provideProductController(ProductRepository<Product> productRepository) {
    return new ProductController(productRepository);
  }

  @Provides
  @Singleton
  public JsonTSportsWatchConverter<User> provideUserConverter() {
    return new JsonToUserConverter();
  }

  @Provides
  @Singleton
  public UserRepository provideUserRepository(MongoClient mongoClient, JsonTSportsWatchConverter<User> userConverter) {
    return new UserRepository(mongoClient, userConverter, USER_COLLECTION);
  }

  @Provides
  @Singleton
  public IToken provideTokenService() {
    return new TokenService();
  }

  @Provides
  @Singleton
  public Auth provideAuthController(UserRepository userRepository, IToken tokenService) {
    return new AuthController(userRepository, tokenService);
  }

  @Provides
  @Singleton
  public IWatchFeatures provideWatchFeaturesRepository(MongoClient mongoClient) {
    return new WatchFeaturesRepository(mongoClient, WATCH_FEATURES);
  }

  @Provides
  @Singleton
  public WatchFeaturesController provideWatchFeaturesController(IWatchFeatures watchFeaturesRepository) {
    return new WatchFeaturesController(watchFeaturesRepository);
  }

  @Provides
  @Singleton
  public ProductPricePublisher provideProductPricePublisher(ProductRepository<Product> productRepository) {
    return new ProductPricePublisher(productRepository);
  }
}
