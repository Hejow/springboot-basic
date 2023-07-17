package com.devcourse.voucher.presentation;

import com.devcourse.global.ApiResponse;
import com.devcourse.voucher.application.VoucherService;
import com.devcourse.voucher.domain.Voucher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @ResponseStatus(OK)
    @GetMapping
    public ApiResponse<List<String>> findAll() {
        List<String> responses = voucherService.findAll();
        return new ApiResponse<>(responses);
    }

    @ResponseStatus(OK)
    @GetMapping("/search")
    public ApiResponse<List<String>> searchAllByCondition(@RequestParam(required = false) Voucher.Type type,
                                                          @RequestParam(value = "expire", required = false)
                                                          @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime expiredAt) {
        List<String> responses = voucherService.searchAllByCondition(type, expiredAt);
        return new ApiResponse<>(responses);
    }
}
