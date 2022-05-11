package com.service.impl;

import com.domain.entity.Account;
import com.domain.entity.Like;
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

    @Override
    public void insertLike(Account account, Long diaryId) {
        Like like = Like.builder()
                .account(account)
                .diary(diaryRepository.findById(diaryId).get())
                .build();
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void deleteLike(Account account, Long diaryId) throws EntityNotFoundException {
        Integer rowCnt = likeRepository.deleteByAccountAndDiaryId(account, diaryId);
        if (rowCnt == 0)
            throw new EntityNotFoundException();
    }

    @Override
    public Long countLikeByDiaryId(Long diaryId) {
        return likeRepository.countByDiaryId(diaryId);
    }
}
