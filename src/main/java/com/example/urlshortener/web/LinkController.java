package com.example.urlshortener.web;

import com.example.urlshortener.model.Link;
import com.example.urlshortener.model.Redirect;
import com.example.urlshortener.model.User;
import com.example.urlshortener.repository.LinkRepository;
import com.example.urlshortener.repository.RedirectRepository;
import com.example.urlshortener.repository.UserRepository;
import com.example.urlshortener.util.LinkUtil;
import com.example.urlshortener.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = LinkController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class LinkController {
  static final String REST_URL = "/api";
  Logger logger = LoggerFactory.getLogger(LinkController.class.getSimpleName());

  private final LinkRepository linkRepository;
  private final UserRepository userRepository;
  private final RedirectRepository redirectRepository;
  private final LinkUtil linkUtil;
  private final TokenUtil tokenUtil;

  @Autowired
  public LinkController(final LinkRepository linkRepository, final UserRepository userRepository,
      final RedirectRepository redirectRepository) {
    this.linkRepository = linkRepository;
    this.userRepository = userRepository;
    this.redirectRepository = redirectRepository;
    this.linkUtil = new LinkUtil();
    this.tokenUtil = new TokenUtil();
  }

  @Operation(summary = "Создание короткой ссылки")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> createShortUrl(@Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
                              @RequestParam Integer userId,
                              @RequestParam String originalUrl,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime createdTo) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("originalUrl", originalUrl);
    if (createdTo != null) {
      params.put("createdTo", String.valueOf(createdTo));
    }
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    String shortUrl = linkUtil.generate();
    logger.info(shortUrl);
    try {
      originalUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    logger.info(originalUrl);
    Link created = new Link(originalUrl, shortUrl, createdTo);
    linkRepository.save(created, userId);
    URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(REST_URL).build().toUri();
    return ResponseEntity.created(uriOfNewResource).body(created);
  }

  @Operation(summary = "Переход по короткой ссылке")
  @GetMapping
  public ResponseEntity<?> redirect(@Parameter(hidden = true)@RequestHeader(value = "Authorization", required = false) String token,
                                          @RequestParam Integer userId,
                                          @RequestParam String shortUrl) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("shortUrl", shortUrl);
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    Link link = linkRepository.getByShortLink(shortUrl, userId);
    if (link != null) {
      if (link.getCreatedTo() != null && link.getCreatedTo().isBefore(LocalDateTime.now())) {
        linkRepository.deleteByShortLink(shortUrl, userId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      HttpHeaders headers = new HttpHeaders();
      headers.add("Location", link.getOriginalUrl());
      Redirect redirect = new Redirect(link.getOriginalUrl(), userId);
      redirectRepository.save(redirect);
      return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(summary = "Удаление короткой ссылки")
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteLink(@Parameter(hidden = true)@RequestHeader(value = "Authorization", required = false) String token,
      @RequestParam Integer userId,
      @RequestParam String shortUrl) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("shortUrl", shortUrl);
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    if (!linkRepository.deleteByShortLink(shortUrl, userId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Получение списка всех переходов")
  @GetMapping("/redirects")
  public ResponseEntity<?> getAllRedirects(@Parameter(hidden = true)@RequestHeader(value = "Authorization", required = false) String token,
      @RequestParam Integer userId) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return ResponseEntity.ok(redirectRepository.getAll());
  }

  @Operation(summary = "Получение количества переходов по ссылке")
  @GetMapping("/count")
  public ResponseEntity<?> getCountRedirect(@Parameter(hidden = true)@RequestHeader(value = "Authorization", required = false) String token,
      @RequestParam Integer userId,
      @RequestParam String originalUrl) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("originalUrl", originalUrl);
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return ResponseEntity.ok(redirectRepository.getCountRedirect(originalUrl));
  }

  @Operation(summary = "Получение количества уникальных переходов по ссылке")
  @GetMapping("/uniqueCount")
  public ResponseEntity<?> getUniqueCountRedirect(@Parameter(hidden = true)@RequestHeader(value = "Authorization", required = false) String token,
      @RequestParam Integer userId,
      @RequestParam String originalUrl) {
    User user = userRepository.get(userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    String key = user.getSecretKey();
    Map<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("originalUrl", originalUrl);
    String createdToken = tokenUtil.createToken(params, key);
    if (token == null || !token.equals(createdToken)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return ResponseEntity.ok(redirectRepository.getUniqueCountRedirect(originalUrl));
  }
}
