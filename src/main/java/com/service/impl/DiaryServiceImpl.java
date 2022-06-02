package com.service.impl;

import com.config.CustomProperty;
import com.domain.EmotionRecogType;
import com.domain.FileType;
import com.domain.PrivacyType;
import com.domain.dto.DiaryDto;
import com.domain.dto.DiaryInfoDto;
import com.domain.dto.FeelingListDto;
import com.domain.entity.Diary;
import com.domain.entity.MediaFile;
import com.domain.entity.Tag;
import com.repository.AccountRepository;
import com.repository.DiaryRepository;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final AccountRepository accountRepository;
    private final CustomProperty customProperty;

    @Override
    public FeelingListDto getFeelingsInYearMonth(Long accountId, int year, int month) {
        FeelingListDto feelingListDto = new FeelingListDto();

        diaryRepository
                .findByAccountIdAndYearMonth(accountId, year, month)
                .forEach((diary) -> {
                    int day = diary.getDate().getDayOfMonth();
                    feelingListDto.getIds()[day] = diary.getId();
                    feelingListDto.getFeelings()[day] = diary.getFeeling();
                    feelingListDto.getFeelingsCount()[diary.getFeeling()]++;
                });

        return feelingListDto;
    }

    @Override
    public void insertDiary(Long accountId, DiaryDto diaryDto) throws IOException {
        MediaFile voice = createMediaFile(diaryDto.getVoice(), FileType.VOICE);
        MediaFile photo = createMediaFile(diaryDto.getPhoto(), FileType.PHOTO);

        File voiceFile = Paths.get(customProperty.getFileSavePath(), voice.getUuid(), voice.getFileName()).toFile();
        EmotionRecogType emotionRecogResult = getEmotionRecogResultFromModelServer(voiceFile);

        Diary diary = Diary.builder()
                .account(accountRepository.findById(accountId).get())
                .voice(voice)
                .photo(photo)
                .text(diaryDto.getText())
                .privacy(diaryDto.getPrivacy())
                .date(diaryDto.getDate())
                .feeling(diaryDto.getFeeling())
                .emotionRecogResult(emotionRecogResult)
                .build();

        if (diaryDto.getTags() != null) {
            diaryDto.getTags()
                    .stream()
                    .map(tagName -> Tag.builder()
                            .diary(diary)
                            .name(tagName)
                            .build())
                    .forEach(diary.getTags()::add);
        }

        diaryRepository.save(diary);
    }

    @Override
    public void updateDiary(Long accountId, Long diaryId, DiaryDto diaryDto) {
        Diary diary = diaryRepository.findById(diaryId).get();

        if (diary.getAccount().getId() != accountId) {
            throw new AccessDeniedException("Access to this diary is denied.");
        }

        diary.setText(diaryDto.getText());
        diary.setPrivacy(diaryDto.getPrivacy());
        diary.setFeeling(diaryDto.getFeeling());
        diary.getTags().clear();
        if (diaryDto.getTags() != null) {
            diaryDto.getTags()
                    .stream()
                    .map(tagName -> Tag.builder()
                            .diary(diary)
                            .name(tagName)
                            .build())
                    .forEach(diary.getTags()::add);
        }
        diaryRepository.save(diary);
    }

    private MediaFile createMediaFile(MultipartFile multipartFile, FileType fileType) throws IOException {
        if (multipartFile == null)
            return null;

        String uuid = UUID.randomUUID().toString();

        multipartFile.transferTo(getFileFromUuidAndMultipartFile(uuid, multipartFile));

        return MediaFile.builder()
                .uuid(uuid)
                .fileName(multipartFile.getOriginalFilename())
                .fileType(fileType)
                .build();
    }

    private File getFileFromUuidAndMultipartFile(String uuid, MultipartFile multipartFile) {
        Path dirPath = Paths.get(customProperty.getFileSavePath(), uuid);
        dirPath.toFile().mkdirs();

        return Paths.get(dirPath.toString(), multipartFile.getOriginalFilename()).toFile();
    }

    @Override
    public void deleteDiary(Long accountId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();

        if (diary.getAccount().getId() != accountId) {
            throw new AccessDeniedException("Access to this diary is denied.");
        }

        diaryRepository.delete(diary);
    }

    @Override
    public DiaryInfoDto findDiary(Long accountId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();

        if (diary.getAccount().getId() != accountId && diary.getPrivacy() == PrivacyType.PRIVATE) {
            throw new AccessDeniedException("Access to this diary is denied.");
        }

        return new DiaryInfoDto(diary, accountId);
    }

    @Override
    public List<DiaryInfoDto> findDiariesByKeyAndPage(String key, Pageable pageable, Long accountId) {
        return diaryRepository.findDiariesPermittedByTagsContains(accountId, key, pageable)
                .stream()
                .map(diary -> new DiaryInfoDto(diary, accountId))
                .collect(Collectors.toList());
    }

    @Override
    public EmotionRecogType getEmotionRecogResultFromModelServer(MultipartFile voice) throws IOException {
        File file = getFileFromUuidAndMultipartFile(UUID.randomUUID().toString(), voice);
        voice.transferTo(file);

        EmotionRecogType result = getEmotionRecogResultFromModelServer(file);

        file.delete();

        return result;
    }

    private EmotionRecogType getEmotionRecogResultFromModelServer(File file) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("audio", new FileSystemResource(file));

        String result = WebClient.create(customProperty.getModelServerHost())
                .method(HttpMethod.GET)
                .uri("/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multiValueMap)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return EmotionRecogType.valueOf(result);
    }
}
