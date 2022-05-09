package com.repository;

import com.domain.entity.Account;
import com.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Modifying
    @Query("DELETE FROM Like l WHERE l.account = :account AND l.diary.id = :diaryId")
    Integer deleteByAccountAndDiaryId(Account account, Long diaryId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.diary.id = :diaryId")
    Long countByDiaryId(Long diaryId);
}
