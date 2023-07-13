package com.devcourse.voucher.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.devcourse.voucher.domain.Voucher.Status.ISSUED;
import static com.devcourse.voucher.domain.Voucher.Status.USED;

public class Voucher {
    public enum Status { USED, ISSUED }
    public enum Type {
        FIXED, PERCENT;

        public boolean isPercent() {
            return this == PERCENT;
        }
    }

    private final UUID id;
    private final DiscountPolicy discountPolicy;
    private final int discount;
    private final LocalDateTime expireAt;
    private final Type type;
    private Status status;

    public Voucher(UUID id, int discount, LocalDateTime expireAt, Type type, Status status) {
        this.id = id;
        this.discountPolicy = createPolicy(type);
        this.discount = discount;
        this.expireAt = expireAt;
        this.type = type;
        this.status = status;
    }

    public static Voucher of(int discount, LocalDateTime expireAt, Type type) {
        return new Voucher(UUID.randomUUID(), discount, expireAt, type, ISSUED);
    }

    public boolean isUsed() {
        return this.status == USED;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public String toText(String delimiter) {
        return id + delimiter +
                discount + delimiter +
                type + delimiter +
                expireAt + delimiter +
                status;
    }

    private DiscountPolicy createPolicy(Type type) {
        if (type.isPercent()) {
            return new PercentDiscountPolicy();
        }

        return new FixedAmountPolicy();
    }
}
