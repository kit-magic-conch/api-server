package com.domain.dto;

import com.domain.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotBlank
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.nickname = account.getNickname();
    }

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