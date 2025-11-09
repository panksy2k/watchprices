package com.affiliation.product.di;

import com.affiliation.product.repository.IProductRepository;
import com.affiliation.product.repository.JsonTSportsWatch;
import com.affiliation.product.repository.Product;
import com.affiliation.product.repository.ProductRepository;
import com.affiliation.product.web.ProductController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Guice module for dependency injection configuration
 */
public class ApplicationModule extends AbstractModule {
    private static final String PRODUCT_SPORTS_WATCH_COLLECTION = "SPORTSWATCH";

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
}
