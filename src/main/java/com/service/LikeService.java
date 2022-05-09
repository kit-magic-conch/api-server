package com.service;

import com.domain.entity.Account;

public interface LikeService {
    void insertLike(Account account, Long id);
}
