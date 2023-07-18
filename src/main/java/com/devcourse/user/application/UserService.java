package com.devcourse.user.application;

import com.devcourse.global.exception.EntityNotFoundException;
import com.devcourse.user.application.dto.GetUserResponse;
import com.devcourse.user.domain.User;
import com.devcourse.user.domain.repository.BlackListRepository;
import com.devcourse.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final String USER_NOT_FOUND = "Accessing Not EXIST User. ID : ";

    private final UserRepository userRepository;
    private final BlackListRepository blackListRepository;

    public UserService(UserRepository userRepository, BlackListRepository blackListRepository) {
        this.userRepository = userRepository;
        this.blackListRepository = blackListRepository;
    }

    public List<GetUserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<String > findAllBlack() {
        return blackListRepository.findAllBlack();
    }

    public void create(String name) {
        userRepository.save(name);
    }

    public void update(UUID id, String name) {
        userRepository.update(id, name);
    }

    public GetUserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + id));
    }

    public void deleteById(UUID id) {
        validateExistsUser(id);
        userRepository.deleteById(id);
    }

    private void validateExistsUser(UUID id) {
        if (userRepository.isNotExistsById(id)) {
            throw new EntityNotFoundException(USER_NOT_FOUND + id);
        }
    }

    private GetUserResponse toResponse(User user) {
        return new GetUserResponse(user.id(), user.name());
    }
}
