package com.controller;

import com.domain.dto.AccountDto;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity register(
            @RequestBody @Valid AccountDto accountDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        // TODO: 회원가입 실패 경우 더 생각해보기
        try {
            accountService.insertAccount(accountDto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // 아이디 중복
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}