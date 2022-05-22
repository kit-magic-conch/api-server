package com.service;

import org.springframework.http.ResponseEntity;

public interface MediaFileService {
    ResponseEntity findMediaFile(Long accountId, Long mediaFileId);
}
