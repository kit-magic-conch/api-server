package com.service;

import com.domain.dto.DiaryDto;
import com.domain.dto.FeelingListDto;
import com.domain.entity.Account;

import java.io.IOException;

public interface DiaryService {
    FeelingListDto getFeelingsInYearMonth(Account account, int year, int month);

    void insertDiary(Long accountId, DiaryDto diaryDto) throws IOException;
}
