package com.affiliation.product.repository;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Collections;
import java.util.List;

public class ProductList<T extends Product> {
  private final List<T> productList;
  private final int totalPages, page, totalSize;

  public ProductList(List<T> productList) {
    this.productList = productList;
    this.totalPages = -1;
    this.page = -1;
    this.totalSize = -1;
  }

  public ProductList(List<T> productList, int totalPages, int page, int totalSize) {
    this.productList = productList;
    this.totalPages = totalPages;
    this.page = page;
    this.totalSize = totalSize;
  }

  @JsonGetter
  public List<T> getProductList() {
    return (productList == null ? Collections.emptyList() : productList);
  }

  @JsonGetter
  public int getTotalPages() {
    return totalPages;
  }

  @JsonGetter
  public int getPage() {
    return page;
  }

  @JsonGetter
  public int getTotalSize() {
    return totalSize;
  }

  @Override
  public String toString() {
    return "ProductList{" +
      "productList=" + productList +
      '}';
  }
}
