package com.affiliation.product.repository.query;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class MultipleCriteria {
  @JsonSetter("criterias")
  private List<ProductCriteria> criteria;

  public List<ProductCriteria> getCriteria() {
    return criteria;
  }

  public void setCriteria(List<ProductCriteria> criteria) {
    this.criteria = criteria;
  }
}
