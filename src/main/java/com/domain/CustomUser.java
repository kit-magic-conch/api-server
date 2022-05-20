package com.domain;

import com.domain.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUser extends User {
    private final Long accountId;

    public CustomUser(Account account) {
        super(account.getUsername(), account.getPassword(),
                Collections.unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER")));
        this.accountId = account.getId();
    }
}
