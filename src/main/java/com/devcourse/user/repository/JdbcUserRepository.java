package com.devcourse.user.repository;

import com.devcourse.global.util.Query;
import com.devcourse.global.util.Query.Table;
import com.devcourse.user.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devcourse.global.util.Query.Table.*;

@Component
class JdbcUserRepository implements UserRepository {
    private final RowMapper<User> userMapper = (resultSet, resultNumber) -> {
        UUID id = UUID.fromString(resultSet.getString("id"));
        String name = resultSet.getString("name");
        return new User(id, name);
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UUID save(String name) {
        UUID id = UUID.randomUUID();
        String sql = Query.builder()
                .insertInto(USERS)
                .values("id", "name")
                .build();

        jdbcTemplate.update(sql, id.toString(), name);
        return id;
    }

    @Override
    public List<User> findAll() {
        String sql = Query.builder()
                .select("*")
                .from(USERS)
                .build();

        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = Query.builder()
                .select("*")
                .from(USERS)
                .where("id")
                .build();

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userMapper, id.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID id) {
        String sql = Query.builder()
                .deleteFrom(USERS)
                .where("id")
                .build();

        jdbcTemplate.update(sql, id.toString());
    }

    @Override
    public void update(UUID id, String name) {
        String sql = Query.builder().
                update(USERS)
                .set("name")
                .where("id")
                .build();

        jdbcTemplate.update(sql, name, id.toString());
    }
}
