package com.domain.dto;

import com.domain.PrivacyType;
import com.domain.entity.Diary;
import com.domain.entity.MediaFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiaryInfoDto {
    private FileSystemResource voice;
    private FileSystemResource photo;
    private String text;
    private PrivacyType privacy;
    private LocalDate date;
    private Integer feeling;
    private List<String> tags;
    private String accountNickname;
    private Integer likeCount;
    private Boolean isLikedByPrincipal;

    public DiaryInfoDto(Diary diary, Long principalId) {
        this.voice = convertMediaFileToFileSystemResource(diary.getVoice());
        this.photo = convertMediaFileToFileSystemResource(diary.getPhoto());

        this.text = diary.getText();
        this.privacy = diary.getPrivacy();
        this.date = diary.getDate();
        this.feeling = diary.getFeeling();
        this.accountNickname = diary.getAccount().getNickname();
        this.likeCount = diary.getLikes().size();

        this.tags = diary.getTags()
                .stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());

        this.isLikedByPrincipal = diary.getLikes()
                .stream()
                .anyMatch(like -> like.getAccount().getId() == principalId);
    }

    private static FileSystemResource convertMediaFileToFileSystemResource(MediaFile mediaFile) {
        if (mediaFile == null)
            return null;

        return new FileSystemResource(Paths.get(
                System.getProperty("catalina.base"),
                mediaFile.getUuid(),
                mediaFile.getFileName()));
    }
}
