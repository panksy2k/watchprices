package com.affiliation.product.repository;

import io.vertx.core.Future;

public interface IWatchFeatures {
  Future<WatchFeature> getWatchFeatures(ProductType productType, String featureName);

  Future<Boolean> setWatchFeatures(WatchFeatures watchFeatures);
}
