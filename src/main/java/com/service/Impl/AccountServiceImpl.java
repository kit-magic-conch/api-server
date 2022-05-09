package com.service.Impl;

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
        return accountRepository.findByUsername(username).isPresent();
    }

    @Override
    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
    }

    @Override
    public void updatePassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }
}
