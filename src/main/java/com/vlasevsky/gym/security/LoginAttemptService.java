package com.vlasevsky.gym.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 3;
    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockoutCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockoutCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockoutCache.put(key, LocalDateTime.now().plusMinutes(5));
        }
    }

    public boolean isBlocked(String key) {
        LocalDateTime lockoutTime = lockoutCache.get(key);
        if (lockoutTime == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(lockoutTime)) {
            lockoutCache.remove(key);
            return false;
        }
        return true;
    }
}