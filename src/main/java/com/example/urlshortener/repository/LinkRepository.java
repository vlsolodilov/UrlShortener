package com.example.urlshortener.repository;

import com.example.urlshortener.model.Link;
import java.util.List;

public interface LinkRepository {
  Link save(Link link, int userId);
  boolean deleteByShortLink(String shortLink, int userId);
  Link get(int id, int userId);
  List<Link> getAll(int userId);
  Link getByShortLink(String shortUrl, int userId);

}
