package com.domain.dto;

import com.domain.EmotionRecogType;
import com.domain.PrivacyType;
import com.domain.entity.Diary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiaryInfoDto {
    private Long diaryId;
    private Long voiceId;
    private Integer voiceDuration;
    private Long photoId;
    private String text;
    private PrivacyType privacy;
    private LocalDate date;
    private Integer feeling;
    private EmotionRecogType emotionRecogResult;
    private List<String> tags;
    private String accountNickname;
    private Integer likeCount;
    private Boolean isLikedByPrincipal;

    public DiaryInfoDto(Diary diary, Long principalId) {
        this.voiceId = diary.getVoice().getId();
        if (diary.getPhoto() == null) {
            this.photoId = null;
        } else {
            this.photoId = diary.getPhoto().getId();
        }

        this.diaryId = diary.getId();
        this.voiceDuration = diary.getVoiceDuration();
        this.text = diary.getText();
        this.privacy = diary.getPrivacy();
        this.date = diary.getDate();
        this.feeling = diary.getFeeling();
        this.emotionRecogResult = diary.getEmotionRecogResult();
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
}
