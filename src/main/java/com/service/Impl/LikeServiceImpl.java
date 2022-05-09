package com.service.Impl;

import com.domain.entity.Account;
import com.domain.entity.Like;
import com.repository.DiaryRepository;
import com.repository.LikeRepository;
import com.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
