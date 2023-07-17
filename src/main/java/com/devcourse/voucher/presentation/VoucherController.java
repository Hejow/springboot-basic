package com.devcourse.voucher.presentation;

import com.devcourse.global.ApiResponse;
import com.devcourse.voucher.application.VoucherService;
import com.devcourse.voucher.application.dto.CreateVoucherRequest;
import com.devcourse.voucher.application.dto.GetVoucherResponse;
import com.devcourse.voucher.domain.Voucher;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
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
    public ApiResponse<List<GetVoucherResponse>> findAll() {
        List<GetVoucherResponse> responses = voucherService.findAll();
        return new ApiResponse<>(responses);
    }

    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ApiResponse<GetVoucherResponse> findById(@PathVariable UUID id) {
        GetVoucherResponse response = voucherService.findById(id);
        return new ApiResponse<>(response);
    }

    @ResponseStatus(OK)
    @GetMapping("/search")
    public ApiResponse searchAllByCondition(@RequestParam(required = false) Voucher.Type type,
                                            @RequestParam(value = "expire", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime expiredAt) {
        List<GetVoucherResponse> responses = voucherService.searchAllByCondition(type, expiredAt);
        return new ApiResponse<>(responses);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid CreateVoucherRequest request) {
        voucherService.create(request);
        return new ApiResponse<>();
    }

    @ResponseStatus(OK)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        voucherService.deleteById(id);
        return new ApiResponse<>();
    }
}
