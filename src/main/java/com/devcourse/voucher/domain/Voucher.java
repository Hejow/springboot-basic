package com.devcourse.voucher.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.devcourse.global.common.Constant.DELIMITER;
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

    @Transient
    private final DiscountPolicy discountPolicy;
    private final int discount;
    private final LocalDateTime expiredAt;
    private final Type type;
    private Status status;

    public Voucher(UUID id, int discount, LocalDateTime expiredAt, Type type, Status status) {
        this.id = id;
        this.discountPolicy = createPolicy(type);
        this.discount = discount;
        this.expiredAt = expiredAt;
        this.type = type;
        this.status = status;
    }

    public Voucher(int discount, LocalDateTime expiredAt, Type type) {
        this.id = UUID.randomUUID();
        this.discountPolicy = createPolicy(type);
        this.discount = discount;
        this.expiredAt = expiredAt;
        this.type = type;
        this.status = ISSUED;
    }

    public boolean isUsed() {
        return this.status == USED;
    }

    public UUID id() {
        return id;
    }

    public int discount() {
        return discount;
    }

    public LocalDateTime expireAt() {
        return expiredAt;
    }

    public Type type() {
        return type;
    }

    public Status status() {
        return status;
    }

    public DiscountPolicy policy() {
        return discountPolicy;
    }

    public String toText() {
        return id + DELIMITER +
                discount + DELIMITER +
                type + DELIMITER +
                expiredAt + DELIMITER +
                status;
    }

    private DiscountPolicy createPolicy(Type type) {
        if (type.isPercent()) {
            return new PercentDiscountPolicy();
        }

        return new FixedAmountPolicy();
    }
}
