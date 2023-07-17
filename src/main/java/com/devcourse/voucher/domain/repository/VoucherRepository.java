package com.devcourse.voucher.domain.repository;

import com.devcourse.voucher.domain.Voucher;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherRepository {
    Voucher save(Voucher voucher);

    List<Voucher> findAll();

    List<Voucher> findAllByCondition(Voucher.Type type, LocalDateTime expiredAt);

    Optional<Voucher> findById(UUID id);

    void updateStatus(UUID id, Voucher.Status status);

    void deleteById(UUID id);

    boolean isNotExistsById(UUID id);
}
