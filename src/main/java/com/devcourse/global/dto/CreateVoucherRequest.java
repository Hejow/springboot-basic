package com.devcourse.global.dto;

import com.devcourse.voucher.domain.Voucher;

import java.time.LocalDateTime;

public record CreateVoucherRequest(
        int discount,
        LocalDateTime expiredAt,
        Voucher.Type type) {
}
