package com.affiliation.product.repository;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.net.URL;
import java.time.Instant;
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
  @JsonSetter
  protected Instant creationDateTime;
  @JsonSetter
  protected Instant updateDateTime;

  protected Product(String id, String modelName, ProductPrice price, ProductType productType, String desc,
                    Map<String, URL> affiliateMarketingDeepURL, Instant creationDateTime, Instant updateDateTime) {
    this.id = id;
    this.modelName = modelName;
    this.price = price;
    this.productType = productType;
    this.description = desc;
    this.affiliateMarketingDeepURL = affiliateMarketingDeepURL;
    this.creationDateTime = creationDateTime;
    this.updateDateTime = updateDateTime;
  }

  @JsonGetter("productType")
  protected abstract ProductType getProductType();

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  @JsonGetter
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonGetter
  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  @JsonGetter
  public ProductPrice getPrice() {
    return this.price;
  }

  public void setPrice(ProductPrice price) {
    this.price = price;
  }

  @JsonGetter
  public String getDescription() {
    return this.description;
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

  @JsonGetter
  public Instant getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(Instant creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  @JsonGetter
  public Instant getUpdateDateTime() {
    return updateDateTime;
  }

  public void setUpdateDateTime(Instant updateDateTime) {
    this.updateDateTime = updateDateTime;
  }
}
