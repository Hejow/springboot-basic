package com.devcourse.user.presentation;

import com.devcourse.global.ApiResponse;
import com.devcourse.user.application.UserService;
import com.devcourse.user.application.dto.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
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
    @GetMapping("/{id}")
    public ApiResponse<GetUserResponse> findById(@PathVariable UUID id) {
        GetUserResponse response = userService.findById(id);
        return new ApiResponse<>(response);
    }

    @ResponseStatus(OK)
    @GetMapping("/black")
    public ApiResponse<List<String>> findAllBlack() {
        List<String> responses = userService.findAllBlack();
        return new ApiResponse<>(responses);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public ApiResponse<Void> create(@ModelAttribute String name) {
        userService.create(name);
        return new ApiResponse<>();
    }

    @ResponseStatus(OK)
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable UUID id,
                                    @ModelAttribute String name) {
        userService.update(id, name);
        return new ApiResponse<>();
    }

    @ResponseStatus(OK)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return new ApiResponse<>();
    }
}
