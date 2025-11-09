package com.affiliation.product.repository;

import com.affiliation.product.repository.query.Pagination;
import com.affiliation.product.repository.query.ProductCriteria;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.List;

public interface IProductRepository<T extends Product> {
  Future<String> saveProduct(JsonObject product, ProductType productType);

  Future<Boolean> productExists(JsonObject uniqueParams, ProductType productType);

  Future<ProductList<T>> findAllProducts(String productType, Pagination pagination);

  Future<ProductList<T>> findAllProducts(String productType);

  Future<JsonObject> findProductById(ProductType productType, String id);

  Future<List<String>> findProductAttribute(ProductType productType, String returnFieldName);

  Future<ProductList<T>> findProductsByCriteria(ProductType productType, List<ProductCriteria> criteria);

}
