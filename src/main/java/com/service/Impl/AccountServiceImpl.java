package com.service.Impl;

import com.domain.dto.AccountDto;
import com.domain.entity.Account;
import com.repository.AccountRepository;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void insertAccount(AccountDto accountDto) throws DataIntegrityViolationException {
        accountDto.encodePassword(passwordEncoder);
        Account account = getEntityFromDto(accountDto);
        accountRepository.save(account);
    }

    @Override
    public boolean isValidLoginInfo(AccountDto accountDto) {
        Optional<Account> optAccount = accountRepository.findByUsername(accountDto.getUsername());
        return optAccount
                .map(account -> passwordEncoder.matches(accountDto.getPassword(), account.getPassword()))
                .orElse(false);
    }

    private Account getEntityFromDto(AccountDto accountDto) {
        return Account.builder()
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .nickname(accountDto.getNickname())
                .email(accountDto.getEmail())
                .build();
    }
}
