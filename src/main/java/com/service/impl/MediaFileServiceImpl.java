package com.service.impl;

import com.config.CustomProperty;
import com.domain.FileType;
import com.domain.PrivacyType;
import com.domain.entity.Diary;
import com.domain.entity.MediaFile;
import com.repository.DiaryRepository;
import com.repository.MediaFileRepository;
import com.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class MediaFileServiceImpl implements MediaFileService {

    private final MediaFileRepository mediaFileRepository;
    private final DiaryRepository diaryRepository;
    private final CustomProperty customProperty;

    @Override
    public ResponseEntity findMediaFile(Long accountId, Long mediaFileId) {
        MediaFile mediaFile = mediaFileRepository.findById(mediaFileId).get();

        Diary diary;

        HttpHeaders headers = new HttpHeaders();

        if (mediaFile.getFileType() == FileType.VOICE) {
            diary = diaryRepository.findByVoice(mediaFile).get();
            headers.setContentType(new MediaType("audio", "wav"));
        } else {    // FileType.PHOTO
            diary = diaryRepository.findByPhoto(mediaFile).get();
            headers.setContentType(MediaType.IMAGE_JPEG);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(mediaFile.getFileName())
                .build();
        headers.setContentDisposition(contentDisposition);

        if (diary.getAccount().getId() != accountId && diary.getPrivacy() == PrivacyType.PRIVATE) {
            throw new AccessDeniedException("Access to this diary is denied.");
        }

        FileSystemResource resource = new FileSystemResource(Paths.get(
                customProperty.getFileSavePath(),
                mediaFile.getUuid(),
                mediaFile.getFileName()));

        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }
}
