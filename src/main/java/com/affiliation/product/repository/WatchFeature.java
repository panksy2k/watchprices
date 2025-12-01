package com.affiliation.product.repository;

import java.util.List;

public class WatchFeature {
  private String featureName;
  private List<String> features;

  public WatchFeature(String featureName, List<String> features) {
    this.featureName = featureName;
    this.features = features;
  }

  public String getFeatureName() {
    return featureName;
  }

  public List<String> getFeatures() {
    return features;
  }

  @Override
  public String toString() {
    return "WatchFeature{" +
      "featureName='" + featureName + '\'' +
      ", features=" + features +
      '}';
  }
}
