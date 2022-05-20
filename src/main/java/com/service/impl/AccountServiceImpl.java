package com.service.impl;

import com.domain.dto.AccountDto;
import com.domain.entity.Account;
import com.repository.AccountRepository;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void insertAccount(AccountDto accountDto) throws DataIntegrityViolationException {
        accountDto.encodePassword(passwordEncoder);
        accountRepository.save(accountDto.toEntity());
    }

    @Override
    public boolean existsId(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public void updateNickname(Long accountId, String nickname) {
        Account account = accountRepository.findById(accountId).get();
        account.setNickname(nickname);
        accountRepository.save(account);
    }

    @Override
    public void updatePassword(Long accountId, String password) {
        Account account = accountRepository.findById(accountId).get();
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public AccountDto getAccountDtoById(Long accountId) {
        return new AccountDto(accountRepository.findById(accountId).get());
    }
}
