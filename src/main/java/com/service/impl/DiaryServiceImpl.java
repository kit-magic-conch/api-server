package com.service.impl;

import com.domain.FileType;
import com.domain.dto.DiaryDto;
import com.domain.dto.FeelingListDto;
import com.domain.entity.Account;
import com.domain.entity.Diary;
import com.domain.entity.MediaFile;
import com.domain.entity.Tag;
import com.repository.AccountRepository;
import com.repository.DiaryRepository;
import com.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final AccountRepository accountRepository;

    @Override
    public FeelingListDto getFeelingsInYearMonth(Account account, int year, int month) {
        FeelingListDto feelingListDto = new FeelingListDto();

        diaryRepository
                .findByAccountAndYearMonth(account, year, month)
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

        Diary diary = Diary.builder()
                .account(accountRepository.findById(accountId).get())
                .voice(voice)
                .photo(photo)
                .text(diaryDto.getText())
                .privacy(diaryDto.getPrivacy())
                .date(diaryDto.getDate())
                .feeling(diaryDto.getFeeling())
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

    private MediaFile createMediaFile(MultipartFile multipartFile, FileType fileType) throws IOException {
        if (multipartFile == null)
            return null;

        String uuid = UUID.randomUUID().toString();

        Path dirPath = Paths.get(System.getProperty("catalina.base"), uuid);
        dirPath.toFile().mkdirs();

        multipartFile.transferTo(
                Paths.get(dirPath.toString(), multipartFile.getOriginalFilename()).toFile());
        return MediaFile.builder()
                .uuid(uuid)
                .fileName(multipartFile.getOriginalFilename())
                .fileType(fileType)
                .build();
    }

    @Override
    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
