package com.service;

import com.domain.EmotionRecogType;
import com.domain.dto.DiaryDto;
import com.domain.dto.DiaryInfoDto;
import com.domain.dto.FeelingListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public interface DiaryService {
    FeelingListDto getFeelingsInYearMonth(Long accountId, int year, int month);

    void insertDiary(Long accountId, DiaryDto diaryDto) throws IOException, UnsupportedAudioFileException;

    void updateDiary(Long accountId, Long diaryId, DiaryDto diaryDto);

    void deleteDiary(Long accountId, Long diaryId);

    DiaryInfoDto findDiary(Long accountId, Long diaryId);

    List<DiaryInfoDto> findDiariesByKeyAndPage(String key, Pageable pageable, Long accountId);

    EmotionRecogType getEmotionRecogResultFromModelServer(MultipartFile voice) throws IOException;
}
