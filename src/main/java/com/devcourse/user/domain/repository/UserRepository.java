package com.devcourse.user.domain.repository;

import com.devcourse.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {
    UUID save(String name);

    List<User> findAll();

    Optional<User> findById(UUID id);

    void deleteById(UUID id);

    void update(UUID id, String name);

    boolean isNotExistsById(UUID id);
}
