package com.domain.entity;

import java.time.LocalDate;

public class Diary {
    private Long id;
    private Account account;
    private MediaFile voice;
    private MediaFile photo;
    private String text;
    private Integer privacy;
    private LocalDate date;
    private Integer feeling;
}
