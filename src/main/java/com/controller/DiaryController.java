package com.controller;

import com.domain.CustomUser;
import com.domain.dto.DiaryDto;
import com.domain.dto.FeelingListDto;
import com.domain.dto.UpdateValidationGroup;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.NoSuchElementException;

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

        return diaryService.getFeelingsInYearMonth(customUser.getAccountId(), year, month);
    }

    @PostMapping("")
    public ResponseEntity saveDiary(@AuthenticationPrincipal CustomUser customUser, @ModelAttribute @Valid DiaryDto diaryDto) {
        try {
            diaryService.insertDiary(customUser.getAccountId(), diaryDto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException e) {
            // 오늘 이미 일기 등록함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateDiary(@PathVariable("id") Long diaryId,
                                      @AuthenticationPrincipal CustomUser customUser,
                                      @ModelAttribute @Validated(UpdateValidationGroup.class) DiaryDto diaryDto) {

        try {
            diaryService.updateDiary(customUser.getAccountId(), diaryId, diaryDto);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable("id") Long diaryId) {
        diaryService.deleteDiary(diaryId);
    }
}
