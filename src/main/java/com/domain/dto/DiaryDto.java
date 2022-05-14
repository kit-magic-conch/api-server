package com.domain.dto;

import com.domain.PrivacyType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryDto {
    @NotNull
    private MultipartFile voice;

    private MultipartFile photo;

    @NotBlank
    private String text;

    @NotNull
    private PrivacyType privacy;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer feeling;

    private List<String> tags;
}
