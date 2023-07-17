package com.devcourse.voucher.application.dto;

import com.devcourse.voucher.domain.Voucher;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetVoucherResponse(
        UUID id,
        int discount,
        LocalDateTime expiredAt,
        Voucher.Type type,
        Voucher.Status status) {
}
