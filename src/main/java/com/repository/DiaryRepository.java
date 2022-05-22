package com.repository;

import com.domain.entity.Diary;
import com.domain.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {
    @Query("SELECT d FROM Diary d WHERE d.account.id = :accountId AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByAccountIdAndYearMonth(Long accountId, int year, int month);

    Optional<Diary> findByVoice(MediaFile voice);

    Optional<Diary> findByPhoto(MediaFile photo);
}
