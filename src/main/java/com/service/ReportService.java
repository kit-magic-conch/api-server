package com.service;

import com.domain.entity.Account;

public interface ReportService {
    void insertReport(Account account, Long diaryId);
}
