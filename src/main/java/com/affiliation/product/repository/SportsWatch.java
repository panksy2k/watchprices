package com.affiliation.product.repository;

import com.affiliation.product.utils.ProductSerializer;
import com.affiliation.product.utils.Serde;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SportsWatch extends Product implements Cloneable, Serializable {
  private final Serde<SportsWatch> serde = new ProductSerializer<>();
  private String dimension;
  private String weight;
  private String displaySize;
  private String displayResolution;
  private String displayType;
  private String screenMaterial;
  private String dialType;
  private String phoneConnectivity;
  private String waterResistance;
  private String workingTemperature;
  private String batteryLifeDailyUse;
  private String chargingTime;
  private String internalMemory;
  private boolean hasDownloadableGlobalMaps;
  private List<String> dataTrackingSensors;
  private List<String> dailyFeatures;
  private List<String> thirdPartyIntegrationApps;
  private List<String> supportedActivities;
  private Color color;

  public SportsWatch(String id, String modelName, ProductPrice price, String desc, Map<String, URL> affiliateURL) {
    super(id, modelName, price, ProductType.SPORTSWATCH, desc, affiliateURL);
  }

  // Private constructor for Builder pattern
  private SportsWatch(Builder builder) {
    super(builder.id, builder.modelName, builder.price, ProductType.SPORTSWATCH, builder.description,
      builder.affiliateMarketingDeepURL);
    this.dimension = builder.dimension;
    this.weight = builder.weight;
    this.displaySize = builder.displaySize;
    this.displayResolution = builder.displayResolution;
    this.displayType = builder.displayType;
    this.screenMaterial = builder.screenMaterial;
    this.dialType = builder.dialType;
    this.phoneConnectivity = builder.phoneConnectivity;
    this.waterResistance = builder.waterResistance;
    this.workingTemperature = builder.workingTemperature;
    this.batteryLifeDailyUse = builder.batteryLifeDailyUse;
    this.chargingTime = builder.chargingTime;
    this.internalMemory = builder.internalMemory;
    this.hasDownloadableGlobalMaps = builder.hasDownloadableGlobalMaps;
    this.dataTrackingSensors = builder.dataTrackingSensors;
    this.dailyFeatures = builder.dailyFeatures;
    this.thirdPartyIntegrationApps = builder.thirdPartyIntegrationApps;
    this.supportedActivities = builder.supportedActivities;
    this.color = builder.color;
  }

  public static SportsWatch withId(String id, SportsWatch transientObj) {
    if (transientObj == null || id == null) {
      return null;
    }

    SportsWatch clonedObj = transientObj.clone();
    if (clonedObj != null) {
      clonedObj.setId(id);
    }

    return clonedObj;
  }

  @Override
  public SportsWatch clone() {
    byte[] b = this.serde.toBytes(this);
    SportsWatch temp = null;
    if (b != null) {
      temp = this.serde.fromBytes(b);
    }

    return temp;
  }


  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getDisplaySize() {
    return displaySize;
  }

  public void setDisplaySize(String displaySize) {
    this.displaySize = displaySize;
  }

  public String getDisplayResolution() {
    return displayResolution;
  }

  public void setDisplayResolution(String displayResolution) {
    this.displayResolution = displayResolution;
  }

  public String getDisplayType() {
    return displayType;
  }

  public void setDisplayType(String displayType) {
    this.displayType = displayType;
  }

  public String getScreenMaterial() {
    return screenMaterial;
  }

  public void setScreenMaterial(String screenMaterial) {
    this.screenMaterial = screenMaterial;
  }

  public String getDialType() {
    return dialType;
  }

  public void setDialType(String dialType) {
    this.dialType = dialType;
  }

  public String getPhoneConnectivity() {
    return phoneConnectivity;
  }

  public void setPhoneConnectivity(String phoneConnectivity) {
    this.phoneConnectivity = phoneConnectivity;
  }

  public String getWaterResistance() {
    return waterResistance;
  }

  public void setWaterResistance(String waterResistance) {
    this.waterResistance = waterResistance;
  }

  public String getWorkingTemperature() {
    return workingTemperature;
  }

  public void setWorkingTemperature(String workingTemperature) {
    this.workingTemperature = workingTemperature;
  }

  public String getBatteryLifeDailyUse() {
    return batteryLifeDailyUse;
  }

  public void setBatteryLifeDailyUse(String batteryLifeDailyUse) {
    this.batteryLifeDailyUse = batteryLifeDailyUse;
  }

  public String getChargingTime() {
    return chargingTime;
  }

  public void setChargingTime(String chargingTime) {
    this.chargingTime = chargingTime;
  }

  public String getInternalMemory() {
    return internalMemory;
  }

  public void setInternalMemory(String internalMemory) {
    this.internalMemory = internalMemory;
  }

  public boolean isHasDownloadableGlobalMaps() {
    return hasDownloadableGlobalMaps;
  }

  public void setHasDownloadableGlobalMaps(boolean hasDownloadableGlobalMaps) {
    this.hasDownloadableGlobalMaps = hasDownloadableGlobalMaps;
  }

  public List<String> getDataTrackingSensors() {
    return dataTrackingSensors;
  }

  public void setDataTrackingSensors(List<String> dataTrackingSensors) {
    this.dataTrackingSensors = dataTrackingSensors;
  }

  public List<String> getDailyFeatures() {
    return dailyFeatures;
  }

  public void setDailyFeatures(List<String> dailyFeatures) {
    this.dailyFeatures = dailyFeatures;
  }

  public List<String> getThirdPartyIntegrationApps() {
    return thirdPartyIntegrationApps;
  }

  public void setThirdPartyIntegrationApps(List<String> thirdPartyIntegrationApps) {
    this.thirdPartyIntegrationApps = thirdPartyIntegrationApps;
  }

  public List<String> getSupportedActivities() {
    return supportedActivities;
  }

  public void setSupportedActivities(List<String> supportedActivities) {
    this.supportedActivities = supportedActivities;
  }

  @Override
  protected ProductType getProductType() {
    return ProductType.SPORTSWATCH;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return "SportsWatch{" +
      "color=" + color +
      ", supportedActivities=" + supportedActivities +
      ", thirdPartyIntegrationApps=" + thirdPartyIntegrationApps +
      ", dailyFeatures=" + dailyFeatures +
      ", dataTrackingSensors=" + dataTrackingSensors +
      ", hasDownloadableGlobalMaps=" + hasDownloadableGlobalMaps +
      ", internalMemory='" + internalMemory + '\'' +
      ", chargingTime='" + chargingTime + '\'' +
      ", batteryLifeDailyUse='" + batteryLifeDailyUse + '\'' +
      ", workingTemperature='" + workingTemperature + '\'' +
      ", waterResistance='" + waterResistance + '\'' +
      ", phoneConnectivity='" + phoneConnectivity + '\'' +
      ", dialType='" + dialType + '\'' +
      ", screenMaterial='" + screenMaterial + '\'' +
      ", displayType='" + displayType + '\'' +
      ", displayResolution='" + displayResolution + '\'' +
      ", displaySize='" + displaySize + '\'' +
      ", weight='" + weight + '\'' +
      ", dimension='" + dimension + '\'' +
      "} " + super.toString();
  }

  public static class Builder {
    // Required fields from Product
    private String id;
    private String modelName;
    private ProductPrice price;
    private String description;
    private Map<String, URL> affiliateMarketingDeepURL;

    // Optional SportsWatch fields
    private String dimension;
    private String weight;
    private String displaySize;
    private String displayResolution;
    private String displayType;
    private String screenMaterial;
    private String dialType;
    private String phoneConnectivity;
    private String waterResistance;
    private String workingTemperature;
    private String batteryLifeDailyUse;
    private String chargingTime;
    private String internalMemory;
    private boolean hasDownloadableGlobalMaps;
    private List<String> dataTrackingSensors;
    private List<String> dailyFeatures;
    private List<String> thirdPartyIntegrationApps;
    private List<String> supportedActivities;
    private Color color;

    public Builder() {
      // Initialize with defaults
    }

    // Required fields setters
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder modelName(String modelName) {
      this.modelName = modelName;
      return this;
    }

    public Builder price(ProductPrice price) {
      this.price = price;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder affiliateMarketingDeepURL(Map<String, URL> affiliateMarketingDeepURL) {
      this.affiliateMarketingDeepURL = affiliateMarketingDeepURL;
      return this;
    }

    // SportsWatch specific fields setters
    public Builder dimension(String dimension) {
      this.dimension = dimension;
      return this;
    }

    public Builder weight(String weight) {
      this.weight = weight;
      return this;
    }

    public Builder displaySize(String displaySize) {
      this.displaySize = displaySize;
      return this;
    }

    public Builder displayResolution(String displayResolution) {
      this.displayResolution = displayResolution;
      return this;
    }

    public Builder displayType(String displayType) {
      this.displayType = displayType;
      return this;
    }

    public Builder screenMaterial(String screenMaterial) {
      this.screenMaterial = screenMaterial;
      return this;
    }

    public Builder dialType(String dialType) {
      this.dialType = dialType;
      return this;
    }

    public Builder phoneConnectivity(String phoneConnectivity) {
      this.phoneConnectivity = phoneConnectivity;
      return this;
    }

    public Builder waterResistance(String waterResistance) {
      this.waterResistance = waterResistance;
      return this;
    }

    public Builder workingTemperature(String workingTemperature) {
      this.workingTemperature = workingTemperature;
      return this;
    }

    public Builder batteryLifeDailyUse(String batteryLifeDailyUse) {
      this.batteryLifeDailyUse = batteryLifeDailyUse;
      return this;
    }

    public Builder chargingTime(String chargingTime) {
      this.chargingTime = chargingTime;
      return this;
    }

    public Builder internalMemory(String internalMemory) {
      this.internalMemory = internalMemory;
      return this;
    }

    public Builder hasDownloadableGlobalMaps(boolean hasDownloadableGlobalMaps) {
      this.hasDownloadableGlobalMaps = hasDownloadableGlobalMaps;
      return this;
    }

    public Builder dataTrackingSensors(List<String> dataTrackingSensors) {
      this.dataTrackingSensors = dataTrackingSensors;
      return this;
    }

    public Builder dailyFeatures(List<String> dailyFeatures) {
      this.dailyFeatures = dailyFeatures;
      return this;
    }

    public Builder thirdPartyIntegrationApps(List<String> thirdPartyIntegrationApps) {
      this.thirdPartyIntegrationApps = thirdPartyIntegrationApps;
      return this;
    }

    public Builder supportedActivities(List<String> supportedActivities) {
      this.supportedActivities = supportedActivities;
      return this;
    }

    public Builder color(Color color) {
      this.color = color;
      return this;
    }

    public SportsWatch build() {
      // Validate required fields
      if (id == null || modelName == null || price == null) {
        throw new IllegalStateException("Required fields (id, modelName, price) must be set");
      }
      return new SportsWatch(this);
    }
  }
}
