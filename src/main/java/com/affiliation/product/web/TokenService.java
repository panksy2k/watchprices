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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenService implements IToken {
  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  private Map<String, User> tokens = new HashMap<>();

  @Override
  public Future<String> encodeToken(String userID, List<String> permissions) {
    String token = UUID.randomUUID().toString();
    LocalDate expiryDate = LocalDate.now(ZoneOffset.UTC).plusDays(10);

    User u = new User(userID, null, null, permissions, expiryDate);
    tokens.put(token, u);

    logger.info("Adding user to token: {}", token);
    return Future.succeededFuture(token);
  }

  @Override
  public Future<User> decodeToken(String token) {
    if (StringUtil.isNullOrEmpty(token)) {
      return Future.failedFuture(new AccessDeniedException("Token do not exist!"));
    }

    logger.info("Fetching user from token: {}", token);

    User user = tokens.get(token);
    if (user == null) {
      logger.error("Token do not match {}, {}", token, tokens.keySet());
      return Future.failedFuture(new AccessDeniedException("Invalid credentials/tokens"));
    }

    return user.getCreationDate().isBefore(LocalDate.now(ZoneOffset.UTC).minusDays(10)) ?
      Future.failedFuture(new AccessDeniedException("Token expired!")) :
      Future.succeededFuture(user);
  }
}
