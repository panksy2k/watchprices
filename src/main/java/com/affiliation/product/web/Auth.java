package com.affiliation.product.web;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

public interface Auth {
  Future<Boolean> signup(String email, String password);

  Future<String> login(String email, String password);

  void authenticate(RoutingContext context);
}
