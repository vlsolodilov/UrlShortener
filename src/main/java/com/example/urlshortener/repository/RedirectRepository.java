package com.example.urlshortener.repository;

import com.example.urlshortener.model.Redirect;
import java.util.List;

public interface RedirectRepository {
  Redirect save(Redirect redirect);
  List<Redirect> getAll();
  Integer getCountRedirect(String url);
  Integer getUniqueCountRedirect(String url);
}
