package com.affiliation.product.web;

import com.affiliation.product.repository.Product;
import com.affiliation.product.repository.ProductRepository;
import com.affiliation.product.repository.ProductType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductPricePublisher {
  private static final Logger logger = LoggerFactory.getLogger(ProductPricePublisher.class);

  private final ProductRepository<Product> productRepository;

  public ProductPricePublisher(ProductRepository<Product> productRepository) {
    this.productRepository = productRepository;
  }

  public void publishPriceUpdate(ProductType productType, Vertx vertx) {
    logger.info("Publishing price update for product type {}", productType);

    vertx.eventBus().consumer("priceUpdateFetch", p -> {
      logger.info("Price update received for product {}", p);
    });

    //Fetch all SPORTSWATCH
    //Put the details into Vertx Bus (publisher)
    //Get the details from Vertx Bus (subscriber)
    //Fetch the Price via Affiliate Link
    //Check if Price is same ? if not then update it by publishing to another Vertx Bus
    //Subscribe the new bus and fetch the new price
    //Update the DB by putting new price against ID

    productRepository.findAllProducts(productType.name())
      .compose(productList -> {
        if (productList == null || productList.getProductList().isEmpty()) {
          return Future.succeededFuture(Collections.emptyList());
        }
        return Future.succeededFuture(productList.getProductList());
      })
      .map(listProduct -> {
        List<JsonObject> pjson = new ArrayList<>();

        for (int i = 0; i < listProduct.size(); i++) {
          Product p = (Product) listProduct.get(i);

          JsonObject jsonObject = new JsonObject();

          Map<String, URL> affiliateMarketingDeepURL = p.getAffiliateMarketingDeepURL();
          if (affiliateMarketingDeepURL != null) {
            for (Map.Entry<String, URL> mkt : affiliateMarketingDeepURL.entrySet()) {
              jsonObject.put("url", mkt.getValue().toString());
            }
          }

          jsonObject.put("id", p.getId());
          jsonObject.put("price", p.getPrice().getCost());
          jsonObject.put("priceCurrency", p.getPrice().getProductCurrency().name());

          pjson.add(jsonObject);
        }

        return pjson;
      }).onSuccess(product -> {
        for (int j = 0; j < product.size(); j++) {
          vertx.eventBus().publish("priceUpdateFetch", product.get(j));
        }
      });
  }
}
