package com.affiliation.product.utils;

import com.affiliation.product.common.ProductException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ProductSerializer<T extends Serializable> implements Serde<T> {

  @Override
  public byte[] toBytes(T obj) {
    if (obj == null) {
      return null;
    }

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
      oos.flush();
      return baos.toByteArray();
    } catch (IOException e) {
      throw new ProductException("Error serializing object to bytes", e);
    }
  }

  @Override
  public T fromBytes(byte[] bytes) {
    if (bytes == null) {
      return null;
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         ObjectInputStream ois = new ObjectInputStream(bais)) {

      @SuppressWarnings("unchecked")
      T obj = (T) ois.readObject();
      return obj;

    } catch (IOException | ClassNotFoundException e) {
      throw new ProductException("Error deserializing bytes to object", e);
    }
  }
}
