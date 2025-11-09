package com.affiliation.product.repository.query;

public enum QueryOperator {
  GT,
  LT,
  IS,
  IN,
  NIN,
  LTE,
  GTE,
  ALL;

  public static String forOperator(QueryOperator type) {
    switch (type) {
      case GT:
        return "$gt";
      case LT:
        return "$lt";
      case IS:
        return "";
      case IN:
        return "$in";
      case NIN:
        return "$nin";
      case LTE:
        return "$lte";
      case GTE:
        return "$gte";
      case ALL:
        return "$all";
      default:
        throw new IllegalArgumentException(type.name());
    }
  }
}
