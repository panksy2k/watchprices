package com.affiliation.product;

import io.vertx.core.json.JsonObject;

public class ProductMockDataHelper {

  public static JsonObject getSportsWatch() {
    // Sample product document
    return new JsonObject()
      .put("name", "COROS NOMAD")
      .put("modelName", "COROS NOMAD")
      .put("description", "COROS NOMAD")
      .put("price", new JsonObject()
        .put("cost", 319)
        .put("productCurrency", "GBP"))
      .put("productType", "SPORTSWATCH")
      .put("color", "GREEN")
      .put("dimension", "47.8 x 47.8 x 14.8mm")
      .put("weight", "61g")
      .put("waterResistance", "5ATM")
      .put("displaySize", "1.3 inches")
      .put("displayResolution", "260 x 260")
      .put("displayType", "3rd Generation Memory-in-Pixel touch screen")
      .put("screenMaterial", "Hardened Mineral Glass")
      .put("dialType", "Digital")
      .put("phoneConnectivity", "Bluetooth")
      .put("batteryLifeDailyUse", "22 Days")
      .put("chargingTime", "Less Than 2 Hours")
      .put("internalMemory", "32GB")
      .put("workingTemperature", "")
      .put("hasDownloadableGlobalMaps", false)
      .put("dataTrackingSensors", new io.vertx.core.json.JsonArray()
        .add("Optical Heart Rate Monitor May not work over tattoos")
        .add("Barometric Altimeter")
        .add("Accelerometer")
        .add("Gyroscope")
        .add("3D Compass")
        .add("Thermometer")
        .add("Optical Pulse Oximeter")
        .add("Electrocardiogram Sensor"))
      .put("dailyFeatures", new io.vertx.core.json.JsonArray()
        .add("Stopwatch")
        .add("Daily Alerts")
        .add("Updateable Firmware")
        .add("Find My Phone")
        .add("Find My Watch")
        .add("Always-On Display")
        .add("HRV")
        .add("Music"))
      .put("thirdPartyIntegrationApps", new io.vertx.core.json.JsonArray()
        .add("Nike Run Club")
        .add("Komoot")
        .add("TrainingPeaks"))
      .put("supportedActivities", new io.vertx.core.json.JsonArray()
        .add("Running")
        .add("Swimming"))
      .put("category", "electronics");

  }
}
