package com.affiliation.product.repository;

import java.util.List;

public class WatchFeatures {
  private ProductType productType;
  private List<WatchFeature> watchFeatures;

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public List<WatchFeature> getWatchFeatures() {
    return watchFeatures;
  }

  public void setWatchFeatures(List<WatchFeature> watchFeatures) {
    this.watchFeatures = watchFeatures;
  }

  @Override
  public String toString() {
    return "WatchFeatures{" +
      "productType=" + productType +
      ", watchFeatures=" + watchFeatures +
      '}';
  }
}
