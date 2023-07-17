package com.devcourse.user.application.dto;

import java.util.UUID;

public record GetUserResponse(UUID id, String name) {
}
