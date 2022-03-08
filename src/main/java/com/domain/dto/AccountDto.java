package com.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class AccountDto {
    private Long id;

    @NotBlank(groups = {ValidationGroup.loginGroup.class})
    private String username;

    @NotBlank(groups = {ValidationGroup.loginGroup.class})
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;
}