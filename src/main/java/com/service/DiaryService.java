package com.service;

import com.domain.dto.FeelingListDto;
import com.domain.entity.Account;

public interface DiaryService {
    FeelingListDto getFeelingsInYearMonth(Account account, int year, int month);
}
