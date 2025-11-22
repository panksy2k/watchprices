package com.affiliation.product.repository;

import java.time.LocalDate;
import java.util.List;

public final class User {

  private final String userId; // MongoDB auto-generated _id
  private final String email;
  private final String password;
  private final String salt;
  private final List<String> permissions;
  private final LocalDate creationDate;

  // Constructor with userId (for users loaded from database)
  public User(String userId, String email, String password, String salt, List<String> permissions,
              LocalDate creationDate) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.salt = salt;
    this.permissions = permissions;
    this.creationDate = creationDate;
  }

  // Constructor without userId (for new users before saving to database)
  public User(String email, String password, String salt, List<String> permissions,
              LocalDate creationDate) {
    this(null, email, password, salt, permissions, creationDate);
  }

  public String getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }
}
