package com.controller;

import com.domain.CustomUser;
import com.domain.dto.DiaryDto;
import com.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("")
    public ResponseEntity likeDiary(@AuthenticationPrincipal CustomUser customUser, @RequestBody DiaryDto diaryDto) {
        try {
            likeService.insertLike(customUser.getAccount(), diaryDto.getId());
            return new ResponseEntity(
                    likeService.countLikeByDiaryId(diaryDto.getId()),
                    HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // 이미 공감함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("")
    public ResponseEntity unlikeDiary(@AuthenticationPrincipal CustomUser customUser, @RequestBody DiaryDto diaryDto) {
        try {
            likeService.deleteLike(customUser.getAccount(), diaryDto.getId());
            return new ResponseEntity(
                    likeService.countLikeByDiaryId(diaryDto.getId()),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // 공감한 기록 없음
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
