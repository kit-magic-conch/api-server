package com.service;

import com.domain.dto.DiaryDto;
import com.domain.dto.DiaryInfoDto;
import com.domain.dto.FeelingListDto;

import java.io.IOException;

public interface DiaryService {
    FeelingListDto getFeelingsInYearMonth(Long accountId, int year, int month);

    void insertDiary(Long accountId, DiaryDto diaryDto) throws IOException;

    void updateDiary(Long accountId, Long diaryId, DiaryDto diaryDto);

    void deleteDiary(Long accountId, Long diaryId);

    DiaryInfoDto findDiary(Long accountId, Long diaryId);
}
