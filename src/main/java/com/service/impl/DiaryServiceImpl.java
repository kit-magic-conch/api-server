package com.service.impl;

import com.domain.dto.FeelingListDto;
import com.domain.entity.Account;
import com.repository.DiaryRepository;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    @Override
    public FeelingListDto getFeelingsInYearMonth(Account account, int year, int month) {
        FeelingListDto feelingListDto = new FeelingListDto();

        diaryRepository
                .findByAccountAndYearMonth(account, year, month)
                .forEach((diary) -> {
                    int day = diary.getDate().getDayOfMonth();
                    feelingListDto.getIds()[day] = diary.getId();
                    feelingListDto.getFeelings()[day] = diary.getFeeling();
                    feelingListDto.getFeelingsCount()[diary.getFeeling()]++;
                });

        return feelingListDto;
    }
}
