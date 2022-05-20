package com.service;

import com.domain.dto.AccountDto;
import org.springframework.dao.DataIntegrityViolationException;

public interface AccountService {
    void insertAccount(AccountDto accountDto) throws DataIntegrityViolationException;

    boolean existsId(String username);

    void updateNickname(Long accountId, String nickname);

    void updatePassword(Long accountId, String password);

    void deleteAccount(Long accountId);

    AccountDto getAccountDtoById(Long accountId);
}