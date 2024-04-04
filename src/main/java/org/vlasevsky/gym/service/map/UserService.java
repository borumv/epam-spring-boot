package org.vlasevsky.gym.service.map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.UserReadDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.UserNotFoundException;
import org.vlasevsky.gym.mapper.mapstruct.UserMapper;
import org.vlasevsky.gym.model.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserCredentialsService userCredentialsService;
    private final UserMapper userMapper;


    public UserReadDto findById(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));


    }
    @Transactional
    public boolean delete(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Delete user by ID: {}", id);
        Optional<User> maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}