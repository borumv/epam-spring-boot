package org.vlasevsky.gym.service.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.mapper.Mapper;
import org.vlasevsky.gym.model.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCredentialsService userCredentialsService;

    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper, CredentialsDto credentialsDto){
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        return userRepository.findById(id)
                .map(mapper::mapFrom);
    }

    public boolean delete (Long id, CredentialsDto credentialsDto){
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Optional<User> maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}