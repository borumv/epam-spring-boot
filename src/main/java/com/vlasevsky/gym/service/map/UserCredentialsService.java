package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.exceptions.AuthenticationException;
import com.vlasevsky.gym.exceptions.UserNotFoundException;
import com.vlasevsky.gym.model.User;
import com.vlasevsky.gym.repository.UserRepository;
import com.vlasevsky.gym.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class UserCredentialsService implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public String generateUsername(String firstName, String lastName) {
        log.info("Generating username for {} {}", firstName, lastName);
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        log.info("Generated username: {}", username);
        return username;
    }

    public String generateRandomPassword() {
        log.info("Generating random password");
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        String generatedPassword = password.toString();
        log.info("Generated random password: {}", generatedPassword);
        return generatedPassword;
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        log.info("Changing password for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new UserNotFoundException(userId.toString());
                });
        user.setPassword(newPassword);
        userRepository.save(user);
        log.info("Password changed successfully for user ID: {}", userId);
    }

    public boolean checkCredentials(CredentialsDto credentialsDto) {
        log.info("Checking credentials for username: {}", credentialsDto.username());
        boolean isValid = userRepository.login(credentialsDto);
        log.info("Credentials for username: {} are valid: {}", credentialsDto.username(), isValid);
        return isValid;
    }

    @Override
    @Transactional
    public boolean login(CredentialsDto credentialsDto) {
        log.info("Logging in user with username: {}", credentialsDto.username());
        boolean isLoggedIn = userRepository.login(credentialsDto);
        log.info("Login successful for username: {}: {}", credentialsDto.username(), isLoggedIn);
        return isLoggedIn;
    }

    @Override
    @Transactional
    public boolean changePassword(CredentialsDto credentialsDto, String newPassword) {
        log.info("Changing password for username: {}", credentialsDto.username());
        if (checkCredentials(credentialsDto)) {
            User user = userRepository.findByUsername(credentialsDto.username())
                    .orElseThrow(() -> {
                        log.warn("Incorrect credentials for username: {}", credentialsDto.username());
                        return new AuthenticationException("Incorrect credentials");
                    });
            user.setPassword(newPassword);
            userRepository.save(user);
            log.info("Password changed successfully for username: {}", credentialsDto.username());
            return true;
        } else {
            log.warn("Failed to change password for username: {} due to incorrect credentials", credentialsDto.username());
            return false;
        }
    }
}
