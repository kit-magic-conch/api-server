package com.service.impl;

import com.domain.entity.Like;
import com.repository.AccountRepository;
import com.repository.DiaryRepository;
import com.repository.LikeRepository;
import com.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    private final DiaryRepository diaryRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;

    @Override
    public void insertLike(Long accountId, Long diaryId) {
        Like like = Like.builder()
                .account(accountRepository.findById(accountId).get())
                .diary(diaryRepository.findById(diaryId).get())
                .build();
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void deleteLike(Long accountId, Long diaryId) throws EntityNotFoundException {
        Integer rowCnt = likeRepository.deleteByAccountIdAndDiaryId(accountId, diaryId);
        if (rowCnt == 0)
            throw new EntityNotFoundException();
    }

    @Override
    public Long countLikeByDiaryId(Long diaryId) {
        return likeRepository.countByDiaryId(diaryId);
    }
}
