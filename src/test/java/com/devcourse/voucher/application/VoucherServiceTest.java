package com.devcourse.voucher.application;

import com.devcourse.global.exception.EntityNotFoundException;
import com.devcourse.voucher.application.dto.CreateVoucherRequest;
import com.devcourse.voucher.domain.Voucher;
import com.devcourse.voucher.domain.repository.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.devcourse.voucher.domain.Voucher.Type.FIXED;
import static com.devcourse.voucher.domain.Voucher.Type.PERCENT;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class VoucherServiceTest {
    @InjectMocks
    private VoucherService voucherService;

    @Mock
    private VoucherRepository voucherRepository;

    private final LocalDateTime invalidExpiration = LocalDateTime.of(2022, 1, 1, 0, 0);
    private final LocalDateTime expiredAt = LocalDateTime.now().plusMonths(1);

    @Test
    @DisplayName("0보다 작은 할인량을 받으면 예외가 발생한다..")
    void validateDiscountAmountTest() {
        // given
        int discountAmount = -1;
        CreateVoucherRequest request = new CreateVoucherRequest(discountAmount, expiredAt, FIXED);

        // when, then
        assertThatThrownBy(() -> voucherService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("0보다 작거나 100보다 큰 할인률을 받으면 예외가 발생한다.")
    @ValueSource(ints = {-1, 101})
    void validateDiscountRateTest(int discountRate) {
        // given
        CreateVoucherRequest request = new CreateVoucherRequest(discountRate, expiredAt, PERCENT);

        // when, then
        assertThatThrownBy(() -> voucherService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("현재보다 과거인 만료일을 받으면 예외가 발생한다.")
    void validateExpirationTest() {
        // given
        int discountAmount = 1_500;
        CreateVoucherRequest request = new CreateVoucherRequest(discountAmount, invalidExpiration, FIXED);

        // when, then
        assertThatThrownBy(() -> voucherService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("생성 요청이 정상적이라면 검증을 통과하고 repository의 save를 호출한다.")
    @CsvSource({"5000, FIXED",
                "50, PERCENT"})
    void createTest(int discount, Voucher.Type type) {
        // given
        CreateVoucherRequest request = new CreateVoucherRequest(discount, expiredAt, type);
        given(voucherRepository.save(any(Voucher.class))).willReturn(any(Voucher.class));

        // when
        voucherService.create(request);

        // then
        then(voucherRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("삭제 요청이 들어오면 repository의 메서드를 총 두번 호출한다.")
    void deleteById_Success_Test() {
        // given
        UUID id = UUID.randomUUID();
        willReturn(false).given(voucherRepository).isNotExistsById(any());
        willDoNothing().given(voucherRepository).deleteById(any());

        // when
        voucherService.deleteById(id);

        // then
        then(voucherRepository).should(times(1)).isNotExistsById(any());
        then(voucherRepository).should(times(1)).deleteById(any());
    }
    @Test
    @DisplayName("존재하지 않는 바우처 삭제 요청이 들어오면 EntityNotFoundException을 던진다.")
    void deleteById_Fail_ByNotExistVoucher() {
        // given
        UUID id = UUID.randomUUID();
        willReturn(true).given(voucherRepository).isNotExistsById(any());

        // when, then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> voucherService.deleteById(id))
                .withMessage("Accessing Not Exist Voucher. ID : " + id);

        then(voucherRepository).should(times(1)).isNotExistsById(any());
        then(voucherRepository).should(times(0)).deleteById(any());
    }
}
