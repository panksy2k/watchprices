package com.affiliation.product.repository;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonToUserConverter implements JsonTSportsWatchConverter<User> {
  private static final Logger logger = LoggerFactory.getLogger(JsonToUserConverter.class);

  @Override
  public User convert(JsonObject document) {
    if (document == null) {
      return null;
    }

    try {
      // Extract user ID from MongoDB's _id field (auto-generated)
      String userId = document.getString("_id");

      String email = document.getString("email");
      String password = document.getString("password");
      String salt = document.getString("salt");

      // Handle permissions array
      List<String> permissions = convertJsonArrayToStringList(document.getJsonArray("permissions"));

      // Handle creation date
      LocalDate creationDate = null;
      if (document.containsKey("creationDate")) {
        String dateValue = document.getString("creationDate");
        creationDate = LocalDate.parse(dateValue);
      }

      return new User(userId, email, password, salt, permissions, creationDate);
    } catch (Exception e) {
      // Log the error or handle it appropriately
      logger.error("Error converting JsonObject to User: {}", e.getMessage());
      return null;
    }
  }

  private List<String> convertJsonArrayToStringList(JsonArray jsonArray) {
    if (jsonArray == null) {
      return null;
    }
    return jsonArray.stream()
      .filter(Objects::nonNull)
      .map(Object::toString)
      .collect(Collectors.toList());
  }
}
