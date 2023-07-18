package com.devcourse.user.application;

import com.devcourse.global.exception.EntityNotFoundException;
import com.devcourse.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("없는 유저를 삭제하려고 하면 EntityNotFoundException을 던져야 한다.")
    void deleteByIdTest() {
        // given
        UUID id = UUID.randomUUID();
        willReturn(true).given(userRepository).isNotExistsById(any());

        // when, then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.deleteById(id));

        then(userRepository).should(times(1)).isNotExistsById(any());
        then(userRepository).should(times(0)).deleteById(any());
    }
}
