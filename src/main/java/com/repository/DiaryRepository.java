package com.repository;

import com.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.account.id = :accountId AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByAccountIdAndYearMonth(Long accountId, int year, int month);
}
