package com.devcourse.voucher.presentation;

import com.devcourse.voucher.application.VoucherService;
import com.devcourse.voucher.application.dto.CreateVoucherRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.devcourse.voucher.domain.Voucher.Type.FIXED;
import static com.devcourse.voucher.domain.Voucher.Type.PERCENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoucherController.class)
class VoucherControllerTest {
    private static final String BASE_URI = "/vouchers";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherService voucherService;

    @Test
    @DisplayName("전체 조회를 요청하면 200 상태와 함께 예상한 값을 받고 서비스가 한번 호출된다.")
    void findAllTest() throws Exception {
        // given
        List<String> responses = List.of();
        given(voucherService.findAll()).willReturn(responses);

        // when
        mockMvc.perform(get(BASE_URI)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload").isEmpty())
                .andDo(print());

        // then
        then(voucherService).should(times(1)).findAll();
    }

    @Nested
    @DisplayName("모든 검색 결과 가져오기 테스트")
    class searchAllByConditionTest {
        private final String searchURI = BASE_URI + "/search";

        @Test
        @DisplayName("파라미터 2개로 검색하면 200과 예상한 값을 받고 서비스가 한번 호출된다.")
        void searchAllByCondition_Success_By2Parameters() throws Exception {
            // given
            LocalDateTime now = LocalDateTime.now();

            List<String> responses = List.of();
            given(voucherService.searchAllByCondition(any(), any())).willReturn(responses);

            // when
            mockMvc.perform(get(searchURI)
                            .param("type", PERCENT.name())
                            .param("expire", now.toString())
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload").isArray())
                    .andExpect(jsonPath("$.payload").isEmpty())
                    .andDo(print());

            // then
            then(voucherService).should(times(1)).searchAllByCondition(any(), any());
        }

        @Test
        @DisplayName("타입으로만 검색하면 200과 예상한 값을 받고 서비스가 한번 호출된다.")
        void searchAllByCondition_Success_ByType() throws Exception {
            // given
            List<String> responses = List.of();
            given(voucherService.searchAllByCondition(any(), any())).willReturn(responses);

            // when
            mockMvc.perform(get(searchURI)
                            .param("type", FIXED.name())
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload").isArray())
                    .andExpect(jsonPath("$.payload").isEmpty())
                    .andDo(print());

            // then
            then(voucherService).should(times(1)).searchAllByCondition(any(), any());
        }

        @Test
        @DisplayName("유효기간으로만 검색하면 200과 예상한 값을 받고 서비스가 한번 호출된다.")
        void searchAllByCondition_Success_ByExpire() throws Exception {
            // given
            LocalDateTime now = LocalDateTime.now();

            List<String> responses = List.of();
            given(voucherService.searchAllByCondition(any(), any())).willReturn(responses);

            // when
            mockMvc.perform(get(searchURI)
                            .param("expire", now.toString())
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload").isArray())
                    .andExpect(jsonPath("$.payload").isEmpty())
                    .andDo(print());

            // then
            then(voucherService).should(times(1)).searchAllByCondition(any(), any());
        }
    }

    @Test
    @DisplayName("생성 요청이 들어오면 201 응답과 함께 서비스가 한번 호출되어야 한다.")
    void createTest() throws Exception {
        // given
        CreateVoucherRequest request = new CreateVoucherRequest(100, LocalDateTime.now(), PERCENT);

        // when
        mockMvc.perform(post(BASE_URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        then(voucherService).should(times(1)).create(any());
    }
}
