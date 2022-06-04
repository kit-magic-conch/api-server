package com.domain.entity;

import com.domain.EmotionRecogType;
import com.domain.PrivacyType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diaries", uniqueConstraints = @UniqueConstraint(columnNames = { "account_id", "date" }))
public class Diary extends BaseTimeEntity {
    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "voice_id", nullable = false)
    private MediaFile voice;

    @Column(nullable = false)
    private Double voiceDuration;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private MediaFile photo;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivacyType privacy;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer feeling;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmotionRecogType emotionRecogResult;

    @Builder.Default
    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<Report> reports = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();
}
