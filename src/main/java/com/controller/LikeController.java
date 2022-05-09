package com.controller;

import com.domain.CustomUser;
import com.domain.dto.DiaryDto;
import com.service.LikeService;
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
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("")
    public ResponseEntity likeDiary(@AuthenticationPrincipal CustomUser customUser, @RequestBody DiaryDto diaryDto) {
        try {
            likeService.insertLike(customUser.getAccount(), diaryDto.getId());
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // 이미 공감함
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}
