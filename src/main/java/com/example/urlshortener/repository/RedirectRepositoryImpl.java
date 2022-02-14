package com.example.urlshortener.repository;

import com.example.urlshortener.model.Redirect;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class RedirectRepositoryImpl implements RedirectRepository{

  private static final BeanPropertyRowMapper<Redirect> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Redirect.class);

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert insertLink;

  @Autowired
  public RedirectRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.insertLink = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("redirects")
        .usingGeneratedKeyColumns("id");

    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  @Transactional
  public Redirect save(Redirect redirect) {
    MapSqlParameterSource map = new MapSqlParameterSource()
        .addValue("id", redirect.getId())
        .addValue("time_redirect", LocalDateTime.now())
        .addValue("url", redirect.getUrl())
        .addValue("user_id", redirect.getUserId());

    Number newId = insertLink.executeAndReturnKey(map);
    redirect.setId(newId.intValue());
    return redirect;
  }

  @Override
  public List<Redirect> getAll() {
    return jdbcTemplate.query("SELECT * FROM redirects", ROW_MAPPER);
  }

  @Override
  public Integer getCountRedirect(String url) {
    MapSqlParameterSource map = new MapSqlParameterSource()
        .addValue("url", url);
    return namedParameterJdbcTemplate.queryForObject("SELECT COUNT(*) FROM redirects WHERE url=:url", map, Integer.class);
  }

  @Override
  public Integer getUniqueCountRedirect(String url) {
    MapSqlParameterSource map = new MapSqlParameterSource()
        .addValue("url", url);
    return namedParameterJdbcTemplate.queryForObject("SELECT COUNT(DISTINCT user_id) FROM redirects WHERE url=:url", map, Integer.class);
  }
}
