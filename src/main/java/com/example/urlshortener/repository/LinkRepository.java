package com.example.urlshortener.repository;

import com.example.urlshortener.model.Link;

public interface LinkRepository {
  Link save(Link link, int userId);
  boolean deleteByShortLink(String shortLink, int userId);
  Link getByShortLink(String shortUrl, int userId);

}
