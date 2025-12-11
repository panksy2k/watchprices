package com.affiliation.product.web;

import com.affiliation.product.common.AccessDeniedException;
import com.affiliation.product.repository.User;
import com.affiliation.product.repository.UserRepository;
import com.google.inject.Inject;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

public class AuthController implements Auth {
  private final UserRepository userRepository;
  private final IToken tokenService;

  @Inject
  public AuthController(UserRepository userRepository, IToken tokenService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  @Override
  public Future<Boolean> signup(String email, String password) {
    if (!"pankaj.d.p@gmail.com".equalsIgnoreCase(email)) {
      return Future.failedFuture(new AccessDeniedException("User is not authorized to signup!"));
    }

    return userRepository.findUser(email).compose(u -> {
      if (u != null && u.getUserId() != null) {
        return Future.failedFuture(new AccessDeniedException("User already exists"));
      }

      String salt = BCrypt.gensalt();
      String hashPassword = BCrypt.hashpw(password, salt);
      User usr =
        new User(null, email, hashPassword, salt, Arrays.asList("ADMIN"), LocalDate.now(ZoneId.systemDefault()));
      return userRepository.saveUser(usr);
    }).map(u -> u.getUserId() != null);
  }

  @Override
  public Future<String> login(String email, String password) {
    return userRepository.findUser(email).compose(u -> {
      if (u == null) {
        return Future.failedFuture(new AccessDeniedException("No User details found!"));
      }

      String inputHashPassword = BCrypt.hashpw(password, u.getSalt());
      boolean isCredentialMatch = u.getPassword().contentEquals(inputHashPassword);
      if (isCredentialMatch) {
        return this.tokenService.encodeToken(u.getUserId(), u.getPermissions());
      }

      return Future.failedFuture(new AccessDeniedException("Credentials in-correct - cannot login!"));
    });
  }

  @Override
  public void authenticate(RoutingContext context) {
    String authorizationToken = context.request().getHeader("Authorization");

    if (StringUtil.isNullOrEmpty(authorizationToken)) {
      throw new AccessDeniedException("Authorization header missing - cannot authenticate!");
    }

    this.tokenService.decodeToken(authorizationToken).onSuccess(u -> {
      u.getPermissions();
      u.getUserId();

      context.setUser(io.vertx.ext.auth.User.create(
        new JsonObject().put("userId", u.getUserId()).put("permissions", u.getPermissions())));
      context.next();
    }).onFailure(context::fail);
  }
}
