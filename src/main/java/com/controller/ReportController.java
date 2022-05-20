package com.controller;

import com.domain.CustomUser;
import com.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("")
    public ResponseEntity reportDiary(@AuthenticationPrincipal CustomUser customUser, @RequestParam Long diaryId) {
        try {
            reportService.insertReport(customUser.getAccountId(), diaryId);
            // TODO: 신고 누적 시 처리
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // 이미 신고함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}
