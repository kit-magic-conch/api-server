package com.controller;

import com.domain.CustomUser;
import com.domain.dto.AccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Test
    @DisplayName("'user' 로그인 성공")
    @WithAnonymousUser
    void loginSuccess() throws Exception {
//        AccountDto accountDto = AccountDto.builder()
//                .username("user")
//                .password("pw")
//                .build();
//        String content = objectMapper.writeValueAsString(accountDto);
        String content = "{\"username\":\"user\",\"password\":\"pw\"}";

        mockMvc.perform(post("/session")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(authenticated()
                        .withUsername("user")
                        .withAuthentication(auth ->
                                assertInstanceOf(UsernamePasswordAuthenticationToken.class, auth)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("'user' 로그인 실패")
    @WithAnonymousUser
    void loginFailure() throws Exception {
//        AccountDto accountDto = AccountDto.builder()
//                .username("user")
//                .password("pwd")
//                .build();
//        String content = objectMapper.writeValueAsString(accountDto);
        String content = "{\"username\":\"user\",\"password\":\"pwd\"}";

        mockMvc.perform(post("/session")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃")
    @WithUserDetails
    void logout() throws Exception {
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated(),
                "로그아웃 전 인증되어 있어야 함");

        mockMvc.perform(delete("/session"))
                .andExpect(status().isNoContent());

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "로그아웃 후 인증 정보가 없어야 함");
    }

    @Test
    @DisplayName("인증 없이 /hello 접속")
    @WithAnonymousUser
    void withoutUser() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("인증 후 /hello 접속")
    @WithUserDetails
    void withUser() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertInstanceOf(CustomUser.class, principal, "principal은 CustomUser 객체여야 함");
        assertEquals("user", ((CustomUser) principal).getUsername(), "'user'가 로그인된 상태여야 함");

        mockMvc.perform(get("/hello"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("principal 정보")
    @WithUserDetails
    void getPrincipal() throws Exception {
        mockMvc.perform(get("/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user")))
                .andExpect(jsonPath("$.nickname", is("nick")));
    }
}