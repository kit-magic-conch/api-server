package com.service;

import com.domain.dto.AccountDto;
import com.domain.entity.Account;
import org.springframework.dao.DataIntegrityViolationException;

public interface AccountService {
    void insertAccount(AccountDto accountDto) throws DataIntegrityViolationException;

    boolean existsId(String username);

    void updateNickname(Account account, String nickname);

    void updatePassword(Account account, String password);
}