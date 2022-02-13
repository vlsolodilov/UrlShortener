package com.example.urlshortener.repository;

import com.example.urlshortener.model.User;

public interface UserRepository {
  User get(int id);
}
