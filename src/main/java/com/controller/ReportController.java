package com.controller;

import com.domain.CustomUser;
import com.domain.dto.DiaryDto;
import com.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("")
    public ResponseEntity reportDiary(@AuthenticationPrincipal CustomUser customUser, @RequestBody DiaryDto diaryDto) {
        try {
            reportService.insertReport(customUser.getAccount(), diaryDto.getId());
            // TODO: 신고 누적 시 처리
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // 이미 신고함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}
