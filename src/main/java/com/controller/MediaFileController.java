package com.controller;

import com.domain.CustomUser;
import com.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mediafiles")
public class MediaFileController {

    private final MediaFileService mediaFileService;

    @GetMapping("/{id}")
    public ResponseEntity getFile(@PathVariable("id") Long mediaFileId,
                                  @AuthenticationPrincipal CustomUser customUser) {

        try {
            return mediaFileService.findMediaFile(customUser.getAccountId(), mediaFileId);
        } catch (AccessDeniedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
