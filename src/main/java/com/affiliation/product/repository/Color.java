package com.affiliation.product.repository;

public enum Color {
  BLACK,
  WHITE,
  RED,
  BLUE,
  GREEN,
  YELLOW,
  ORANGE,
  PURPLE,
  PINK,
  GREY;

  public static Color fromValue(String value) {
    if (value == null) {
      return null;
    }

    try {
      return Color.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null; // or handle as needed
    }
  }
}
