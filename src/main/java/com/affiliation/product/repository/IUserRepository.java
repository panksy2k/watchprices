package com.affiliation.product.repository;

import io.vertx.core.Future;

public interface IUserRepository {
  Future<User> saveUser(User user);

  Future<User> findUser(String email);
}
