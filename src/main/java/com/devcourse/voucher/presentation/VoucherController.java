package com.devcourse.voucher.presentation;

import com.devcourse.global.ApiResponse;
import com.devcourse.voucher.application.VoucherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
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
}
