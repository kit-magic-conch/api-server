package com.controller;

import com.domain.CustomUser;
import com.domain.EmotionRecogType;
import com.domain.dto.DiaryDto;
import com.domain.dto.DiaryInfoDto;
import com.domain.dto.FeelingListDto;
import com.domain.dto.UpdateValidationGroup;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity getDiary(@PathVariable("id") Long diaryId,
                                   @AuthenticationPrincipal CustomUser customUser) {

        try {
            DiaryInfoDto diaryInfoDto = diaryService.findDiary(customUser.getAccountId(), diaryId);
            return new ResponseEntity(diaryInfoDto, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public List<DiaryInfoDto> getDiariesByKeyAndPage(
            @RequestParam(required = false) String key,
            @PageableDefault(sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUser customUser) {

        return diaryService.findDiariesByKeyAndPage(key, pageable, customUser.getAccountId());
    }

    @PostMapping("")
    public ResponseEntity saveDiary(@AuthenticationPrincipal CustomUser customUser, @ModelAttribute @Valid DiaryDto diaryDto) {
        try {
            diaryService.insertDiary(customUser.getAccountId(), diaryDto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException e) {
            // 오늘 이미 일기 등록함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateDiary(@PathVariable("id") Long diaryId,
                                      @AuthenticationPrincipal CustomUser customUser,
                                      @RequestBody @Validated(UpdateValidationGroup.class) DiaryDto diaryDto) {

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
    public ResponseEntity deleteDiary(@PathVariable("id") Long diaryId,
                                      @AuthenticationPrincipal CustomUser customUser) {
        try {
            diaryService.deleteDiary(customUser.getAccountId(), diaryId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/emotionRecogResult")
    public EmotionRecogType getEmotionRecogResultFromModelServer(@RequestParam MultipartFile voice) throws IOException {
        return diaryService.getEmotionRecogResultFromModelServer(voice);
    }
}
