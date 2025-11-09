package com.affiliation.product.web;

import com.affiliation.product.repository.ProductList;
import com.affiliation.product.repository.ProductRepository;
import com.affiliation.product.repository.ProductType;
import com.affiliation.product.repository.query.Pagination;
import com.affiliation.product.repository.query.ProductCriteria;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductController {
  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Future<String> createProduct(RoutingContext context) {
    JsonObject jsonObject = context.body().asJsonObject();
    if (jsonObject == null || jsonObject.isEmpty()) {
      return Future.failedFuture("No JSONObject found representing Product");
    }

    //Check if the product already exist
    CompletionStage<String> productType = CompletableFuture.supplyAsync(() -> jsonObject.getString("productType"));

    return Future.fromCompletionStage(productType).map(ProductType::fromValue).compose(ptype -> {
      if (ptype == ProductType.NONE) {
        return Future.failedFuture("ProductType unknown: " + jsonObject.getString("productType"));
      }

      if (ptype == ProductType.SPORTSWATCH) {
        String modelName = jsonObject.getString("modelName");
        String color = jsonObject.getString("color");
        if (modelName == null || color == null) {
          return Future.failedFuture("Model name and color are required for SportsWatch");
        }

        return productRepository.productExists(jsonObject, ptype)
          .compose(status -> {
            if (Boolean.TRUE.equals(status)) {
              return Future.failedFuture("Product already exists");
            }
            return productRepository.saveProduct(jsonObject, ptype);
          });
      }

      return Future.failedFuture("ProductType not supported: " + ptype);
    });
  }

  public Future<ProductList> findAllProducts(RoutingContext context) {
    // For now, we only support SPORTSWATCH products
    ProductType type = ProductType.fromValue(context.pathParam("productType"));
    if (type == ProductType.NONE) {
      return Future.failedFuture("ProductType unknown: " + context.pathParam("productType"));
    }

    logger.info("Pagination - limit: {}, page: {}", context.queryParam("limit"), context.queryParam("page"));

    Optional<Integer> pageValue = context.queryParam("page").stream()
      .filter(this::isValidInteger)
      .map(Integer::valueOf)
      .findFirst();
    Optional<Integer> limitValue = context.queryParam("limit").stream()
      .filter(this::isValidInteger)
      .map(Integer::valueOf)
      .findFirst();

    if (type == ProductType.SPORTSWATCH) {
      if (pageValue.isPresent() && limitValue.isPresent()) {
        return productRepository.findAllProducts(ProductType.SPORTSWATCH.name(),
          new Pagination(pageValue.get(), limitValue.get()));
      } else {
        return productRepository.findAllProducts(ProductType.SPORTSWATCH.name());
      }
    }

    return Future.failedFuture("ProductType not supported: " + type);
  }

  public Future<JsonObject> findProductById(RoutingContext context) {
    CompletionStage<Map.Entry<String, String>> productTypeId =
      CompletableFuture.supplyAsync(() ->
        Map.entry(context.pathParam("productType"), context.pathParam("id")));

    return Future.fromCompletionStage(productTypeId).compose(entry -> {
      String productType = entry.getKey();
      String id = entry.getValue();

      ProductType ptype = ProductType.fromValue(productType);
      if (ptype == ProductType.NONE) {
        return Future.failedFuture("ProductType unknown: " + productType);
      }

      if (ptype == ProductType.SPORTSWATCH) {
        return productRepository.findProductById(ptype, id);
      }

      return Future.failedFuture("ProductType not supported: " + ptype);
    });
  }

  public Future<String> updateProduct(RoutingContext context) {
    JsonObject toBeModified = context.body().asJsonObject();
    if (toBeModified == null || toBeModified.isEmpty() || StringUtil.isNullOrEmpty(context.pathParam("id"))) {
      return Future.failedFuture("No JSONObject found representing Product or product Id not found!");
    }

    ProductType contextProductType = ProductType.fromValue(context.pathParam("productType"));
    if (contextProductType == null || contextProductType.equals(ProductType.NONE)) {
      return Future.failedFuture("ProductType cannot be determined!");
    }

    //Check if the product already exist
    Map.Entry<ProductType, String> entry = Map.entry(contextProductType, context.pathParam("id"));

    logger.info("Updating product with data: {}", toBeModified.toString());

    Future<Map.Entry<ProductType, String>> productTypeId =
      Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> entry));

    return productTypeId.compose(ptypeidEntry -> {
      logger.info("Processing product type: {}", ptypeidEntry.getKey());

      if (ptypeidEntry.getKey() == ProductType.SPORTSWATCH) {
        String productID = ptypeidEntry.getValue();

        return productRepository.findProductById(entry.getKey(), productID);
      }

      return Future.failedFuture("ProductType not supported: " + ptypeidEntry.getValue());
    }).compose(existJson -> {
      if (existJson == null) {
        return Future.failedFuture("Product not found!");
      }
      return productRepository.saveProduct(toBeModified, contextProductType);
    });
  }

  public Future<List<String>> findProductAttribute(RoutingContext context) {
    String ptype = context.pathParam("productType");
    if (ptype == null || ptype.isEmpty()) {
      return Future.failedFuture("No Product Type found");
    }

    String aname = context.pathParam("attributeName");
    if (aname == null || aname.isEmpty()) {
      return Future.failedFuture("No attribute type found!");
    }

    ProductType type = ProductType.fromValue(ptype);
    if (type == ProductType.NONE) {
      return Future.failedFuture("ProductType unknown: " + ptype);
    }

    if (type == ProductType.SPORTSWATCH) {
      return productRepository.findProductAttribute(type, aname);
    }

    return Future.failedFuture("Product not supported");
  }

  private boolean isValidInteger(String value) {
    if (value == null || value.trim().isEmpty()) {
      return false;
    }
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      logger.warn("Invalid integer value: {}", value);
      return false;
    }
  }

  public Future<ProductList> findProductByCriteria(RoutingContext context) {
    JsonObject queryCriteria = context.body().asJsonObject();
    String productType = context.pathParam("productType");

    if (queryCriteria == null || queryCriteria.isEmpty() || StringUtil.isNullOrEmpty(productType)) {
      return Future.failedFuture("No JSONObject found representing Product or productType not found!");
    }

    ProductType type = ProductType.fromValue(productType);
    JsonObject jsonPayload = context.body().asJsonObject();
    List<ProductCriteria> criteriaList = new ArrayList<>();

    if (jsonPayload.containsKey("criterias")) {
      JsonArray jsonArray = jsonPayload.getJsonArray("criterias");

      for (int i = 0; i < jsonArray.size(); i++) {
        criteriaList.add(jsonArray.getJsonObject(i).mapTo(ProductCriteria.class));
      }
    }

    if (criteriaList.isEmpty()) {
      return Future.failedFuture("No criteria found!");
    }

    logger.info("Searching for criteria: {}", criteriaList.toString());
    return productRepository.findProductsByCriteria(type, criteriaList);
  }
}
