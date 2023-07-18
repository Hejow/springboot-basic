package com.devcourse.voucher.domain.repository;

import com.devcourse.global.util.Sql;
import com.devcourse.voucher.domain.Voucher;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.devcourse.global.util.Sql.Table.VOUCHERS;

@Component
@Profile("dev")
class JdbcVoucherRepository implements VoucherRepository {
    private final RowMapper<Voucher> voucherMapper = (resultSet, resultNumber) -> {
        UUID id = UUID.fromString(resultSet.getString("id"));
        int discount = Integer.parseInt(resultSet.getString("discount"));
        LocalDateTime expiredAt = resultSet.getTimestamp("expired_at").toLocalDateTime();
        Voucher.Type type = Voucher.Type.valueOf(resultSet.getString("type"));
        Voucher.Status status = Voucher.Status.valueOf(resultSet.getString("status"));
        return new Voucher(id, discount, expiredAt, type, status);
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoucherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Voucher save(Voucher voucher) {
        String sql = Sql.builder()
                .insertInto(VOUCHERS)
                .values("id", "discount", "expired_at", "type", "status")
                .build();

        jdbcTemplate.update(sql,
                voucher.id().toString(),
                voucher.discount(),
                voucher.expireAt(),
                voucher.type().name(),
                voucher.status().name());

        return voucher;
    }

    @Override
    public List<Voucher> findAll() {
        String sql = Sql.builder()
                .select("*")
                .from(VOUCHERS)
                .build();

        return jdbcTemplate.query(sql, voucherMapper);
    }

    @Override
    public List<Voucher> findAllByCondition(Voucher.Type type, LocalDateTime expiredAt) {
        Sql.FromBuilder sql = Sql.builder()
                .select("*")
                .from(VOUCHERS);

        if (type == null && expiredAt == null) {
            return Collections.emptyList();
        }

        if (type != null && expiredAt == null) {
            sql.where("type");
            return jdbcTemplate.query(sql.build(), voucherMapper, type.name());
        }

        if (type == null && expiredAt != null) {
            sql.where("expired_at");
            return jdbcTemplate.query(sql.build(), voucherMapper, expiredAt);
        }

        sql.where("type").and("expired_at");
        return jdbcTemplate.query(sql.build(), voucherMapper, type.name(), expiredAt);
    }

    @Override
    public Optional<Voucher> findById(UUID id) {
        String sql = Sql.builder()
                .select("*")
                .from(VOUCHERS)
                .where("id")
                .build();

        List<Voucher> result = jdbcTemplate.query(sql, voucherMapper, id.toString());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }

    @Override
    public void updateStatus(UUID id, Voucher.Status status) {
        String sql = Sql.builder()
                .update(VOUCHERS)
                .set("status")
                .where("id")
                .build();

        jdbcTemplate.update(sql, status.name(), id.toString());
    }

    @Override
    public void deleteById(UUID id) {
        String sql = Sql.builder()
                .deleteFrom(VOUCHERS)
                .where("id")
                .build();

        jdbcTemplate.update(sql, id.toString());
    }

    @Override
    public boolean isNotExistsById(UUID id) {
        String sql = Sql.builder()
                .select("*")
                .from(VOUCHERS)
                .where("id")
                .limit(1)
                .build();

        List<Voucher> result = jdbcTemplate.query(sql, voucherMapper, id.toString());
        return result.isEmpty();
    }
}
