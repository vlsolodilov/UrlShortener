package com.example.urlshortener.repository;

import com.example.urlshortener.model.Link;
import java.util.List;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class LinkRepositoryImpl implements LinkRepository {

    private static final RowMapper<Link> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Link.class);

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert insertLink;

    public LinkRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.insertLink = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("links")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Link save(Link link, int userId) {
        //ValidationUtil.validate(link);

        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", link.getId())
                .addValue("original_url", link.getOriginalUrl())
                .addValue("short_url", link.getShortUrl())
                .addValue("created_to", link.getCreatedTo())
                .addValue("user_id", userId);


        Number newId = insertLink.executeAndReturnKey(map);
        link.setId(newId.intValue());
        return link;
    }

    @Override
    @Transactional
    public boolean deleteByShortLink(String shortLink, int userId) {
        return jdbcTemplate.update("DELETE FROM links WHERE short_url=? AND user_id=?", shortLink, userId) != 0;
    }

    @Override
    public Link get(int id, int userId) {
        List<Link> links = jdbcTemplate.query(
                "SELECT * FROM links WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(links);
    }

    @Override
    public List<Link> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM links WHERE user_id=?", ROW_MAPPER, userId);
    }

    @Override
    public Link getByShortLink(String shortUrl, int userId) {
        List<Link> links = jdbcTemplate.query(
            "SELECT * FROM links WHERE short_url = ? AND user_id = ?", ROW_MAPPER, shortUrl, userId);
        return DataAccessUtils.singleResult(links);
    }

}
