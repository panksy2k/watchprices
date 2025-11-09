package com.affiliation.product.repository;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class JsonTSportsWatch implements JsonTSportsWatchConverter<SportsWatch> {

  @Override
  public SportsWatch convert(JsonObject document) {
    if (document == null) {
      return null;
    }

    try {
      SportsWatch.Builder builder = new SportsWatch.Builder()
          .id(document.getString("_id"))
          .modelName(document.getString("modelName"))
          .description(document.getString("description"))
          .dimension(document.getString("dimension"))
          .weight(document.getString("weight"))
          .displaySize(document.getString("displaySize"))
          .displayResolution(document.getString("displayResolution"))
          .displayType(document.getString("displayType"))
          .screenMaterial(document.getString("screenMaterial"))
          .dialType(document.getString("dialType"))
          .phoneConnectivity(document.getString("phoneConnectivity"))
          .waterResistance(document.getString("waterResistance"))
          .workingTemperature(document.getString("workingTemperature"))
          .batteryLifeDailyUse(document.getString("batteryLifeDailyUse"))
          .chargingTime(document.getString("chargingTime"))
          .internalMemory(document.getString("internalMemory"));

      // Handle boolean field
      Boolean hasDownloadableGlobalMaps = document.getBoolean("hasDownloadableGlobalMaps");
      if (hasDownloadableGlobalMaps != null) {
        builder.hasDownloadableGlobalMaps(hasDownloadableGlobalMaps);
      }

      // Handle price
      Double cost = document.getDouble("cost");
      String currency = document.getString("currency");
      if (cost != null && currency != null) {
        ProductPrice.ProductCurrency productCurrency = ProductPrice.ProductCurrency.fromValue(currency);
        if (productCurrency != null) {
          builder.price(new ProductPrice(cost, productCurrency));
        }
      }

      // Handle color
      String colorStr = document.getString("color");
      if (colorStr != null) {
        Color color = Color.fromValue(colorStr);
        if (color != null) {
          builder.color(color);
        }
      }

      // Handle JSON arrays
      builder.dataTrackingSensors(convertJsonArrayToStringList(document.getJsonArray("dataTrackingSensors")))
             .dailyFeatures(convertJsonArrayToStringList(document.getJsonArray("dailyFeatures")))
             .thirdPartyIntegrationApps(convertJsonArrayToStringList(document.getJsonArray("thirdPartyIntegrationApps")))
             .supportedActivities(convertJsonArrayToStringList(document.getJsonArray("supportedActivities")));

      // Handle affiliate URLs
      JsonObject affiliateUrlsJson = document.getJsonObject("affiliateMarketingDeepURL");
      if (affiliateUrlsJson != null) {
        Map<String, URL> affiliateUrls = convertJsonObjectToUrlMap(affiliateUrlsJson);
        builder.affiliateMarketingDeepURL(affiliateUrls);
      }

      return builder.build();
    } catch (Exception e) {
      // Log the error or handle it appropriately
      System.err.println("Error converting JsonObject to SportsWatch: " + e.getMessage());
      return null;
    }
  }

  private List<String> convertJsonArrayToStringList(JsonArray jsonArray) {
    if (jsonArray == null) {
      return null;
    }
    return jsonArray.stream()
        .filter(Objects::nonNull)
        .map(Object::toString)
        .collect(Collectors.toList());
  }

  private Map<String, URL> convertJsonObjectToUrlMap(JsonObject jsonObject) {
    if (jsonObject == null) {
      return null;
    }

    Map<String, URL> urlMap = new HashMap<>();
    for (String key : jsonObject.fieldNames()) {
      try {
        String urlString = jsonObject.getString(key);
        if (urlString != null && !urlString.isEmpty()) {
          urlMap.put(key, new URL(urlString));
        }
      } catch (MalformedURLException e) {
        // Skip malformed URLs, could log warning here
        System.err.println("Malformed URL for key " + key + ": " + e.getMessage());
      }
    }

    return urlMap.isEmpty() ? null : urlMap;
  }

}
