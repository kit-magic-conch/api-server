package com.controller;

import com.domain.CustomUser;
import com.domain.dto.AccountDto;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

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

    @GetMapping("/{username}/exists")
    public boolean existsId(@PathVariable String username) {
        return accountService.existsId(username);
    }

    @PatchMapping("/nickname")
    public void updateNickname(@AuthenticationPrincipal CustomUser customUser, @RequestBody AccountDto accountDto) {
        accountService.updateNickname(customUser.getAccountId(), accountDto.getNickname());
    }

    @PatchMapping("/password")
    public void updatePassword(@AuthenticationPrincipal CustomUser customUser, @RequestBody AccountDto accountDto) {
        accountService.updatePassword(customUser.getAccountId(), accountDto.getPassword());
    }

    @DeleteMapping("")
    public void deleteAccount(
            @AuthenticationPrincipal CustomUser customUser,
            HttpServletRequest request
    ) throws ServletException {

        accountService.deleteAccount(customUser.getAccountId());
        request.logout();
    }
}