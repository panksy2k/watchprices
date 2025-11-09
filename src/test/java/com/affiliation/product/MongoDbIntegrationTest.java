package com.affiliation.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(VertxExtension.class)
@Testcontainers
public class MongoDbIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0")
            .withExposedPorts(27017);

    private Vertx vertx;
    private MongoClient mongoClient;

    @BeforeEach
    void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();

        // Configure MongoDB client with testcontainer connection string
        JsonObject config = new JsonObject()
                .put("connection_string", mongoDBContainer.getConnectionString())
                .put("db_name", "testdb");

        mongoClient = MongoClient.createShared(vertx, config);
        testContext.completeNow();
    }

    @AfterEach
    void tearDown(VertxTestContext testContext) {
        if (mongoClient != null) {
            mongoClient.close();
        }
        if (vertx != null) {
            vertx.close().onComplete(testContext.succeedingThenComplete());
        } else {
            testContext.completeNow();
        }
    }

    @Test
    void shouldInsertAndRetrieveProduct(VertxTestContext testContext) {
        // Sample product document
        JsonObject product = ProductMockDataHelper.getSportsWatch();

        // Insert the product
        mongoClient.insert("products", product)
                .compose(insertId -> {
                    assertNotNull(insertId);
                    // Query to find the inserted product
                    JsonObject query = new JsonObject().put("_id", insertId);
                    return mongoClient.findOne("products", query, null);
                })
                .onSuccess(retrievedProduct -> {
                    assertNotNull(retrievedProduct);
                    assertEquals("COROS NOMAD", retrievedProduct.getString("name"));
                    assertEquals("COROS NOMAD", retrievedProduct.getString("modelName"));
                    assertEquals("electronics", retrievedProduct.getString("category"));

                    // Verify nested price object
                    JsonObject priceObject = retrievedProduct.getJsonObject("price");
                    assertNotNull(priceObject);
                    assertEquals(319, priceObject.getInteger("cost"));
                    assertEquals("GBP", priceObject.getString("productCurrency"));

                    // Verify some other fields
                    assertEquals("SPORTSWATCH", retrievedProduct.getString("productType"));
                    assertEquals("GREEN", retrievedProduct.getString("color"));
                    assertEquals(false, retrievedProduct.getBoolean("hasDownloadableGlobalMaps"));

                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    public void shouldUpdateProduct(VertxTestContext testContext) {
        JsonObject product = ProductMockDataHelper.getSportsWatch();

        mongoClient.insert("products", product)
                .compose(insertId -> {
                    JsonObject query = new JsonObject().put("_id", insertId);
                    JsonObject update = new JsonObject().put("$set",
                            new JsonObject()
                                    .put("name", "COROS Vertis 2S")
                                    .put("price", new JsonObject().put("cost", 600).put("productCurrency", "EUR")));

                    return mongoClient.updateCollection("products", query, update)
                            .compose(result -> mongoClient.findOne("products", query, null));
                })
                .onSuccess(updatedProduct -> {
                    assertNotNull(updatedProduct);
                    assertEquals("COROS Vertis 2S", updatedProduct.getString("name"));
                    assertEquals(600.0, updatedProduct.getJsonObject("price").getDouble("cost"), 0.001);
                    assertEquals("EUR", updatedProduct.getJsonObject("price").getString("productCurrency"));

                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    public void shouldDeleteProduct(VertxTestContext testContext) {
        JsonObject product = ProductMockDataHelper.getSportsWatch();

        mongoClient.insert("products", product)
                .compose(insertId -> {
                    JsonObject query = new JsonObject().put("_id", insertId);
                    return mongoClient.removeDocument("products", query)
                            .compose(result -> mongoClient.findOne("products", query, null));
                })
                .onSuccess(deletedProduct -> {
                    assertNull(deletedProduct);
                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    public void shouldCountDocuments(VertxTestContext testContext) {
      Future<String> product_1 =
        mongoClient.insert("products", ProductMockDataHelper.getSportsWatch());
      Future<String> product_2 =
        mongoClient.insert("products", ProductMockDataHelper.getSportsWatch());
      Future<String> product_3 =
        mongoClient.insert("products", ProductMockDataHelper.getSportsWatch());

      Future<CompositeFuture> all = CompositeFuture.join(product_1, product_2, product_3);
      testContext.verify(() -> {
        all.onSuccess(r -> {
          String s1 = r.resultAt(0);
          String s2 = r.resultAt(1);
          String s3 = r.resultAt(2);

          assertNotNull(s1);
          assertNotNull(s2);
          assertNotNull(s3);

          testContext.completeNow();
        }).onFailure(testContext::failNow);
      });
    }
}
