package com.domain.dto;

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
    private Long voiceId;
    private Long photoId;
    private String text;
    private PrivacyType privacy;
    private LocalDate date;
    private Integer feeling;
    private List<String> tags;
    private String accountNickname;
    private Integer likeCount;
    private Boolean isLikedByPrincipal;

    public DiaryInfoDto(Diary diary, Long principalId) {
        this.voiceId = diary.getVoice().getId();
        this.photoId = diary.getPhoto().getId();

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
}
