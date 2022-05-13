package com.controller;

import com.domain.CustomUser;
import com.domain.dto.FeelingListDto;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/feelings")
    public FeelingListDto getFeelingList(
            @AuthenticationPrincipal CustomUser customUser,
            @RequestParam int year,
            @RequestParam int month) {

        return diaryService.getFeelingsInYearMonth(customUser.getAccount(), year, month);
    }
}
