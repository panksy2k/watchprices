package com.affiliation.product.repository;

import com.affiliation.product.repository.query.Pagination;
import com.affiliation.product.repository.query.ProductCriteria;
import com.affiliation.product.repository.query.QueryOperator;
import com.google.inject.Inject;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductRepository<T extends Product> implements IProductRepository<T> {
  private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
  private final MongoClient mongoClient;
  private Function<JsonObject, T> converter = jo -> (T) new JsonTSportsWatch().convert(jo);

  private ProductType sportsWatchProductType = ProductType.SPORTSWATCH;

  @Inject
  // Constructor for specific product type with converter
  public ProductRepository(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Override
  public Future<String> saveProduct(JsonObject product, ProductType productType) {
    if (product == null) {
      return Future.failedFuture("No JSONObject found representing Product");
    }

    if (productType == sportsWatchProductType) {
      logger.info("Saving product of type: {}", productType);
      return mongoClient.save(sportsWatchProductType.name(), product);
    }

    return Future.failedFuture("ProductType not supported by this repository: " + productType);
  }

  @Override
  public Future<Boolean> productExists(JsonObject uniqueParams, ProductType productType) {
    JsonObject query = new JsonObject();

    switch (productType) {
      case SPORTSWATCH: {
        String modelName = uniqueParams.getString("modelName");
        Color color = Color.fromValue(uniqueParams.getString("color"));

        if (StringUtil.isNullOrEmpty(modelName) || Objects.isNull(color)) {
          return Future.failedFuture("Model name and color are required for SportsWatch to be checked for existence");
        }

        query.put("modelName", modelName);
        query.put("color", color.name());
        query.put("_id", uniqueParams.getString("_id"));

        return mongoClient.find(ProductType.SPORTSWATCH.name(), query).map(records -> {
          if (records == null || records.isEmpty()) {
            return false;
          }

          return records.size() > 0;
        });
      }
    }

    return Future.failedFuture("ProductType unknown: " + productType);
  }

  @Override
  public Future<ProductList<T>> findAllProducts(String productType) {
    ProductType ptype = ProductType.fromValue(productType);
    if (ptype == null || ptype == ProductType.NONE) {
      return Future.failedFuture("ProductType unknown: " + productType);
    }

    switch (ptype) {
      case SPORTSWATCH: {
        return mongoClient.find(sportsWatchProductType.name(), new JsonObject())
          .map(lj -> lj.stream()
            .map(converter)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()))
          .map(ProductList::new);
      }

      default:
        return Future.failedFuture("ProductType unknown: " + ptype);
    }
  }

  @Override
  public Future<ProductList<T>> findAllProducts(String productType, Pagination pagination) {
    ProductType ptype = ProductType.fromValue(productType);
    if (ptype == null || ptype == ProductType.NONE) {
      return Future.failedFuture("ProductType unknown: " + productType);
    }

    switch (ptype) {
      case SPORTSWATCH: {
        return mongoClient.find(sportsWatchProductType.name(), new JsonObject())
          .map(lj -> lj.stream()
            .map(converter)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()))
          .map(list -> {
            int total = list.size();
            int pagesTotal =
              total % pagination.getLimit() == 0 ? total / pagination.getLimit() : (total / pagination.getLimit()) + 1;
            int start = (pagination.getPage() - 1) * pagination.getLimit();

            return new ProductList(list.stream().skip(start).limit(pagination.getLimit()).collect(Collectors.toList()),
              pagesTotal, pagination.getPage(), total);
          });
      }

      default:
        return Future.failedFuture("ProductType unknown: " + ptype);
    }
  }

  @Override
  public Future<JsonObject> findProductById(ProductType productType, String id) {
    if (productType == null) {
      return Future.failedFuture("ProductType unknown: " + productType);
    }

    if (StringUtil.isNullOrEmpty(id)) {
      return Future.failedFuture("Product ID cannot be null or empty");
    }

    JsonObject query = new JsonObject().put("_id", id);

    switch (productType) {
      case SPORTSWATCH: {
        return mongoClient.findOne(sportsWatchProductType.name(), query, null)
          .compose(result -> {
            if (result == null) {
              return Future.failedFuture("Product not found with ID: " + id);
            }
            return Future.succeededFuture(result);
          });
      }

      default:
        return Future.failedFuture("ProductType unknown: " + productType);
    }
  }

  @Override
  public Future<List<String>> findProductAttribute(ProductType productType, String returnFieldName) {
    if (productType == null) {
      return Future.failedFuture("ProductType cannot be null");
    }

    if (StringUtil.isNullOrEmpty(returnFieldName)) {
      return Future.failedFuture("Return field name cannot be null or empty");
    }

    switch (productType) {
      case SPORTSWATCH: {
        return mongoClient.distinct(sportsWatchProductType.name(), returnFieldName, String.class.getName())
          .map(distinctValues -> {
            if (distinctValues == null) {
              return List.<String>of();
            }

            return distinctValues.stream()
              .filter(Objects::nonNull)
              .map(String::valueOf)
              .collect(Collectors.toList());
          })
          .recover(throwable -> {
            logger.error("Error fetching distinct values for field '{}' in collection '{}': {}",
              returnFieldName, productType.name(), throwable.getMessage());
            return Future.succeededFuture(List.of());
          });
      }

      case NONE:
        return Future.failedFuture("ProductType NONE is not supported");

      default:
        return Future.failedFuture("ProductType unknown: " + productType);
    }
  }

  @Override
  public Future<ProductList<T>> findProductsByCriteria(ProductType productType, List<ProductCriteria> criteria) {
    JsonObject query = new JsonObject();

    for (ProductCriteria productCriteria : criteria) {
      QueryOperator operator = productCriteria.getOperator();
      String column = productCriteria.getColumn();

      String mongoOperator = QueryOperator.forOperator(operator);
      if (mongoOperator.isEmpty()) {
        query.put(column, productCriteria.getValue());
      } else {
        query.put(column, new JsonObject().put(mongoOperator, productCriteria.getValue()));
      }
    }

    logger.info("Sending query to mongo: {}", query.toString());

    switch (productType) {
      case SPORTSWATCH: {
        return mongoClient.find(sportsWatchProductType.name(), query)
          .map(l -> l.stream().map(converter).collect(Collectors.toList()))
          .map(ProductList::new);
      }

      case NONE:
        return Future.failedFuture("ProductType NONE is not supported");
      default:
        return Future.failedFuture("ProductType unknown: " + productType);
    }
  }
}
