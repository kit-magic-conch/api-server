package com.domain.entity;

import com.domain.PrivacyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diaries")
public class Diary extends BaseTimeEntity {
    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id", nullable = false)
    private MediaFile voice;

    @OneToOne(fetch = FetchType.LAZY)
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
}
