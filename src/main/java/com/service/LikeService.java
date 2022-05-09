package com.service;

import com.domain.entity.Account;

public interface LikeService {
    void insertLike(Account account, Long diaryId);
    void deleteLike(Account account, Long diaryId);
    Long countLikeByDiaryId(Long diaryId);
}
