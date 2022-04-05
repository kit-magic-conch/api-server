package com.service;

import com.domain.dto.AccountDto;
import org.springframework.dao.DataIntegrityViolationException;

public interface AccountService {
    void insertAccount(AccountDto accountDto) throws DataIntegrityViolationException;
}