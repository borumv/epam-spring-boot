package com.vlasevsky.gym.security;

import com.vlasevsky.gym.exceptions.AuthenticationException;
import com.vlasevsky.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Value("${auth.message.blocked}")
    private String blockedMessage;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new AuthenticationException(blockedMessage);
        }
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

    }
}