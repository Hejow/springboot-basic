package com.devcourse.voucher.application.dto;

import com.devcourse.voucher.domain.Voucher;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateVoucherRequest(
        @NotNull
        int discount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expiredAt,
        Voucher.Type type) {
}
