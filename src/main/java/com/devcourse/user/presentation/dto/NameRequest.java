package com.devcourse.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record NameRequest(@NotBlank String name) {
}
