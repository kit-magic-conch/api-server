package com.repository;

import com.domain.entity.Account;
import com.domain.entity.Diary;
import com.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.account = :account AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByAccountAndYearMonth(Account account, int year, int month);

//    List<Tag> findTagByDiaryId(Long diaryId);
}
