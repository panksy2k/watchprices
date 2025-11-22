package com.affiliation.product.web;

import com.affiliation.product.common.AccessDeniedException;
import com.affiliation.product.repository.User;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenService implements IToken {

  private Map<String, User> tokens = new HashMap<>();

  @Override
  public Future<String> encodeToken(String userID, List<String> permissions) {
    String token = UUID.randomUUID().toString();
    LocalDate expiryDate = LocalDate.now(ZoneOffset.UTC).plusDays(10);

    User u = new User(userID, null, null, permissions, expiryDate);
    tokens.put(token, u);

    return Future.succeededFuture(token);
  }

  @Override
  public Future<User> decodeToken(String token) {
    if (StringUtil.isNullOrEmpty(token)) {
      return Future.failedFuture(new AccessDeniedException("Token do not exist!"));
    }

    User user = tokens.get(token);
    if (user == null) {
      return Future.failedFuture(new AccessDeniedException("Invalid credentials/tokens"));
    }

    return user.getCreationDate().isBefore(LocalDate.now(ZoneOffset.UTC).minusDays(10)) ?
      Future.failedFuture(new AccessDeniedException("Token expired!")) :
      Future.succeededFuture(user);
  }
}
