package com.vlasevsky.gym.repository.mockbeans;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.model.User;
import com.vlasevsky.gym.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class UserRepositoryImpl implements UserRepository {
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public Optional<User> checkCredentials(CredentialsDto dto) {
        return Optional.empty();
    }

    @Override
    public boolean login(CredentialsDto dto) {
        return false;
    }

    @Override
    public void updateFailedAttempt(int failedAttempt, String username) {

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
