package com.affiliation.product.web;

import com.affiliation.product.repository.User;
import io.vertx.core.Future;
import java.util.List;

public interface IToken {
  Future<String> encodeToken(String userID, List<String> permissions);

  Future<User> decodeToken(String token);
}
