package com.devcourse.user.application;

import com.devcourse.user.application.dto.GetUserResponse;
import com.devcourse.user.domain.User;
import com.devcourse.user.domain.repository.BlackListRepository;
import com.devcourse.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
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

    private GetUserResponse toResponse(User user) {
        return new GetUserResponse(user.id(), user.name());
    }
}
