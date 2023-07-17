package com.devcourse.user.presentation;

import com.devcourse.user.application.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String BASE_URI = "/users";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("모든 유저들을 요청하면 200 응답과 함께 서비스를 한번만 호출해야 한다.")
    void findAllTest() throws Exception {
        // given
        willReturn(Collections.emptyList()).given(userService).findAll();

        // when
        mockMvc.perform(get(BASE_URI)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload").isEmpty())
                .andDo(print());

        // then
        then(userService).should(times(1)).findAll();
    }

    @Test
    @DisplayName("블랙리스트를 호출하면 200 응답과 함께 서비스를 한번 호출해야 한다.")
    void findAllBlackTest() throws Exception {
        // given
        List<String> list = new ArrayList<>(List.of("test1", "test2"));
        willReturn(list).given(userService).findAllBlack();

        // when
        mockMvc.perform(get(BASE_URI + "/black")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload").value(list))
                .andDo(print());

        // then
        then(userService).should(times(1)).findAllBlack();
    }
}
