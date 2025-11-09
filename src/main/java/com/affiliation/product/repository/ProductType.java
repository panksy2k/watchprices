package com.affiliation.product.repository;

public enum ProductType {
  SPORTSWATCH,
  NONE;

  public static ProductType fromValue(String value) {
    if (value == null) {
      return NONE;
    }

    switch (value.toUpperCase()) {
      case "SPORTSWATCH":
        return SPORTSWATCH;
      default:
        return NONE;
    }
  }
}
