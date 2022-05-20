package com.service;

public interface LikeService {
    void insertLike(Long accountId, Long diaryId);

    void deleteLike(Long accountId, Long diaryId);

    Long countLikeByDiaryId(Long diaryId);
}
