package com.devcourse.user.presentation;

import com.devcourse.global.ApiResponse;
import com.devcourse.user.application.UserService;
import com.devcourse.user.application.dto.GetUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(OK)
    @GetMapping
    public ApiResponse<List<GetUserResponse>> findAll() {
        List<GetUserResponse> responses = userService.findAll();
        return new ApiResponse<>(responses);
    }

    @ResponseStatus(OK)
    @GetMapping("/black")
    public ApiResponse<List<String>> findAllBlack() {
        List<String> responses = userService.findAllBlack();
        return new ApiResponse<>(responses);
    }
}
