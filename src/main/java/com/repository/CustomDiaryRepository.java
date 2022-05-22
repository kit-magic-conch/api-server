package com.repository;

import com.domain.entity.Diary;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomDiaryRepository {
    List<Diary> findDiariesPermittedByTagsContains(Long accountId, String key, Pageable pageable);
}
