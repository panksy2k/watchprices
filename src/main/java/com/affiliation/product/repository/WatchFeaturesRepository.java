package com.affiliation.product.repository;

import com.google.inject.Inject;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchFeaturesRepository implements IWatchFeatures {
  private static final Logger logger = LoggerFactory.getLogger(WatchFeaturesRepository.class);

  private final MongoClient mongoClient;
  private final String collectionName;

  @Inject
  // Constructor with mongo client and converter
  public WatchFeaturesRepository(MongoClient mongoClient, String collectionName) {
    this.mongoClient = mongoClient;
    this.collectionName = collectionName;
  }

  @Override
  public Future<WatchFeature> getWatchFeatures(ProductType productType, String featureName) {
    if (productType == null || StringUtil.isNullOrEmpty(featureName)) {
      return Future.failedFuture("In-sufficient details -- cannot determine what feature to be fetched!");
    }

    return mongoClient.distinctWithQuery(this.collectionName, featureName, String.class.getName(),
        new JsonObject().put("productType", productType.name()))
      .map(ja -> {
        if (ja.isEmpty()) {
          logger.error("No {} found in documents!", featureName);
          return new WatchFeature(featureName, Collections.emptyList());
        }

        List<String> allFeatureValues = ja.stream().map(Object::toString).collect(Collectors.toList());

        logger.info("{} found in documents!", allFeatureValues);
        return new WatchFeature(featureName, allFeatureValues);
      });
  }

  @Override
  public Future<Boolean> setWatchFeatures(WatchFeatures watchFeatures) {
    if (watchFeatures == null) {
      return Future.failedFuture("Cannot persist null object!");
    }

    return mongoClient.findOne(collectionName,
        new JsonObject().put("productType", watchFeatures.getProductType().name()), null)
      .compose(existObj -> {
        if (existObj == null) {
          JsonObject newObj = new JsonObject().put("productType", watchFeatures.getProductType().name());
          for (WatchFeature allFeatureTypes : watchFeatures.getWatchFeatures()) {
            newObj.put(allFeatureTypes.getFeatureName(), allFeatureTypes.getFeatures());
          }

          logger.info("Persisting new object: {}", newObj);
          return mongoClient.save(collectionName, newObj).map(id -> id != null);
        } else {
          JsonObject updateObj = new JsonObject();

          for (WatchFeature allFeatureTypes : watchFeatures.getWatchFeatures()) {
            updateObj.put(allFeatureTypes.getFeatureName(), allFeatureTypes.getFeatures());
          }

          logger.info("Updating existing watch features in documents!");
          return mongoClient.updateCollection(collectionName,
            new JsonObject().put("productType", watchFeatures.getProductType().name()),
            new JsonObject().put("$set", updateObj)).map(res -> res.getDocUpsertedId() != null);
        }
      });
  }
}
