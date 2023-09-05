package ru.netology.jdbc_dao.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
        import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Value("classpath:product_name.sql")
    private Resource productQuery;

    public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getProductName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        try (InputStream is = productQuery.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            String query = bufferedReader.lines().collect(Collectors.joining("\n"));
            return jdbcTemplate.queryForObject(query, params, String.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
