package com.service.Impl;

import com.domain.CustomUser;
import com.domain.entity.Account;
import com.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optAccount = accountRepository.findByUsername(username);
        return optAccount
                .map(CustomUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found"));
    }
}
