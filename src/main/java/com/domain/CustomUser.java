package com.domain;

import com.domain.entity.Account;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomUser extends User {
    private final Account account;

    public CustomUser(Account account) {
        super(account.getUsername(), account.getPassword(),
                Collections.unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER")));
        this.account = account;
    }
}
