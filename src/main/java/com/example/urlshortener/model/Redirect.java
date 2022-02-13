package com.example.urlshortener.model;

import java.time.LocalDateTime;

public class Redirect {
  Integer id;
  LocalDateTime timeRedirect;
  String url;
  Integer userId;

  public Redirect() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getTimeRedirect() {
    return timeRedirect;
  }

  public void setTimeRedirect(LocalDateTime timeRedirect) {
    this.timeRedirect = timeRedirect;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
