package com.domain.dto;

import com.domain.entity.Account;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder
public class AccountDto {
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    public String encodePassword(PasswordEncoder passwordEncoder) {
        return password = passwordEncoder.encode(password);
    }

    public Account toEntity() {
        return Account.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}