package com.affiliation.product.repository;

import com.google.inject.Inject;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository implements IUserRepository {
  private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

  private final MongoClient mongoClient;
  private final String collectionName;
  private final JsonTSportsWatchConverter<User> userConverter;

  @Inject
  // Constructor with mongo client and converter
  public UserRepository(MongoClient mongoClient, JsonTSportsWatchConverter<User> userConverter, String collectionName) {
    this.mongoClient = mongoClient;
    this.collectionName = collectionName;
    this.userConverter = userConverter;
  }

  @Override
  public Future<User> saveUser(User user) {
    if (user == null) {
      return Future.failedFuture("User is null - cannot save!");
    }

    JsonObject userJson = new JsonObject();
    userJson.put("email", user.getEmail());
    userJson.put("password", user.getPassword());
    userJson.put("salt", user.getSalt());
    userJson.put("permissions", new JsonArray(user.getPermissions()));
    // Convert LocalDate to ISO-8601 String for MongoDB storage
    if (user.getCreationDate() != null) {
      userJson.put("creationDate", user.getCreationDate().toString());
    }

    return mongoClient.save(this.collectionName, userJson)
      .compose(idStr -> mongoClient.findOne(this.collectionName, new JsonObject().put("_id", idStr), null))
      .compose(res -> {
        if (res == null) {
          return Future.failedFuture("User not found after save");
        }

        User storedUser = userConverter.convert(res);
        if (storedUser == null) {
          return Future.failedFuture("Failed to convert saved user from database");
        }

        return Future.succeededFuture(storedUser);
      });
  }

  @Override
  public Future<User> findUser(String email) {
    if (StringUtil.isNullOrEmpty(email)) {
      return Future.failedFuture("Email is null or empty - cannot fetch user!");
    }

    return mongoClient.findOne(this.collectionName, new JsonObject().put("email", email), null)
      .map(result -> {
        if (result == null) {
          return new User(null, null, null, null, null, null);
        }

        return userConverter.convert(result);
      });
  }
}
