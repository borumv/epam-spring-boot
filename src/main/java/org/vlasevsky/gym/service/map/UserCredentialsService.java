package org.vlasevsky.gym.service.map;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.UserNotFoundException;
import org.vlasevsky.gym.model.User;
import org.vlasevsky.gym.service.UserService;

import java.util.Random;

@Service
@AllArgsConstructor
public class UserCredentialsService implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    @Transactional

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean checkCredentials(CredentialsDto credentialsDto) {
        return userRepository.login(credentialsDto);
    }

    @Override
    @Transactional
    public boolean login(CredentialsDto credentialsDto) {
        return userRepository.login(credentialsDto);
    }

    @Override
    @Transactional
    public boolean changePassword(CredentialsDto credentialsDto, String newPassword) {
        if (checkCredentials(credentialsDto)) {
            User user = userRepository.findByUsername(credentialsDto.username())
                    .orElseThrow(() -> new AuthenticationException("Incorrect credentials"));
            user.setPassword(newPassword);
            return true;
        } else return false;
    }
}
