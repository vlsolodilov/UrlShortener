package com.example.urlshortener.model;

import java.time.LocalDateTime;

public class Link {

  private Integer id;
  private String originalUrl;
  private String shortUrl;
  private LocalDateTime createdTo;


  public Link() {
  }

  public Link(Integer id, String originalUrl, String shortUrl, LocalDateTime createdTo,
      Integer countRedirect) {
    this.id = id;
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
    this.createdTo = createdTo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public LocalDateTime getCreatedTo() {
    return createdTo;
  }

  public void setCreatedTo(LocalDateTime createdTo) {
    this.createdTo = createdTo;
  }
}
