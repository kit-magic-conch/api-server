package com.controller;

import com.domain.dto.AccountDto;
import com.domain.dto.ValidationGroup;
import com.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity login(
            @RequestBody @ModelAttribute @Validated(ValidationGroup.loginGroup.class) AccountDto accountDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(accountService.isValidLoginInfo(accountDto) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
}
