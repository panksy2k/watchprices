package com.affiliation.product.utils;

import java.io.Serializable;

public interface Serde<T> extends Serializable {
  T fromBytes(byte[] b);
  byte[] toBytes(T obj);
}
