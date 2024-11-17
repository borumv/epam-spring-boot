package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository{
    List<User> findAll();
    boolean existsByUsername(@Param("username") String username);

    Optional<User> checkCredentials(@Param("dto") CredentialsDto dto);

    boolean login(@Param("dto") CredentialsDto dto);


    void updateFailedAttempt(int failedAttempt, String username);
    Optional<User> findByUsername(String username);

    void save(User user);

    void deleteById(Long id);
}
