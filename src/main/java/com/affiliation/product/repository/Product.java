package com.affiliation.product.repository;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.net.URL;
import java.util.Map;

public abstract class Product {
  @JsonSetter
  protected String id;
  @JsonSetter
  protected String modelName;
  @JsonSetter
  protected ProductType productType;
  @JsonSetter
  protected ProductPrice price;
  @JsonSetter
  protected String description;
  @JsonSetter
  protected Map<String, URL> affiliateMarketingDeepURL;

  protected Product(String id, String modelName, ProductPrice price, ProductType productType, String desc,
                    Map<String, URL> affiliateMarketingDeepURL) {
    this.id = id;
    this.modelName = modelName;
    this.price = price;
    this.productType = productType;
    this.description = desc;
    this.affiliateMarketingDeepURL = affiliateMarketingDeepURL;
  }

  @JsonGetter("productType")
  protected abstract ProductType getProductType();

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public String getModelName() {
    return modelName;
  }

  @JsonGetter
  public ProductPrice getPrice() {
    return this.price;
  }

  @JsonGetter
  public String getDescription() {
    return this.description;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public void setPrice(ProductPrice price) {
    this.price = price;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonGetter
  public Map<String, URL> getAffiliateMarketingDeepURL() {
    return affiliateMarketingDeepURL;
  }

  public void setAffiliateMarketingDeepURL(Map<String, URL> affiliateMarketingDeepURL) {
    this.affiliateMarketingDeepURL = affiliateMarketingDeepURL;
  }
}
