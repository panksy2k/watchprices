package com.affiliation.product.repository.query;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class ProductCriteria {
  @JsonSetter("col")
  private String column;

  @JsonSetter("val")
  private List value;

  @JsonSetter("op")
  private QueryOperator operator;

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public List getValue() {
    return value;
  }

  public void setValue(List value) {
    this.value = value;
  }

  public QueryOperator getOperator() {
    return operator;
  }

  public void setOperator(QueryOperator operator) {
    this.operator = operator;
  }

  @Override
  public String toString() {
    return "ProductCriteria{" +
      "column='" + column + '\'' +
      ", value=" + value +
      ", operator=" + operator +
      '}';
  }
}
