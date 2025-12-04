package com.affiliation.product.web;

import com.affiliation.product.repository.IWatchFeatures;
import com.affiliation.product.repository.ProductType;
import com.affiliation.product.repository.WatchFeature;
import com.affiliation.product.repository.WatchFeatures;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import java.util.Arrays;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchFeaturesController {
  private static final Logger logger = LoggerFactory.getLogger(WatchFeaturesController.class);
  private final IWatchFeatures watchFeaturesRepository;

  public WatchFeaturesController(IWatchFeatures watchFeaturesRepository) {
    this.watchFeaturesRepository = watchFeaturesRepository;
  }

  public Future<Boolean> putWatchFeatures() {
    WatchFeatures watchFeatures = new WatchFeatures();

    watchFeatures.setProductType(ProductType.SPORTSWATCH);

    WatchFeature dailyFeature = new WatchFeature("dailyFeatures",
      Stream.of("Daily Stress",
        "Wellness Check",
        "Message/Call Alert",
        "Alarm",
        "Timer",
        "Stopwatch",
        "Daily Data",
        "Updateable Firmware",
        "Find My Phone",
        "Find My Watch",
        "Backlight Always-On Mode",
        "Heart Rate Technology (HRV)",
        "Music").toList());

    WatchFeature thirdParty = new WatchFeature("thirdPartyIntegration",
      Stream.of("Strava",
        "Training Peaks",
        "Relive",
        "Runna",
        "Asics Runkeeper",
        "Decathlon",
        "Nike Run Club",
        "Adidas Running",
        "Komoot",
        "Final Surge").toList());

    watchFeatures.setWatchFeatures(Arrays.asList(dailyFeature, thirdParty));

    return this.watchFeaturesRepository.setWatchFeatures(watchFeatures);
  }

  public Future<WatchFeature> getFeature(String productType, String featureName) {
    ProductType type = ProductType.fromValue(productType);
    if (type == ProductType.NONE) {
      return Future.failedFuture("ProductType unknown: " + productType);
    }

    if (StringUtil.isNullOrEmpty(featureName)) {
      return Future.failedFuture("Feature Name path param is mandatory!");
    }

    return this.watchFeaturesRepository.getWatchFeatures(type, featureName);
  }
}
