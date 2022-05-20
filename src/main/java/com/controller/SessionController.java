package com.controller;

import com.domain.CustomUser;
import com.domain.dto.AccountDto;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
public class SessionController {
    private final AccountService accountService;

    @GetMapping("")
    public AccountDto getPrincipal(@AuthenticationPrincipal CustomUser customUser) {
        return accountService.getAccountDtoById(customUser.getAccountId());
    }
}
