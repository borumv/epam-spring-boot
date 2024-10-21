package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.exceptions.UserNotFoundException;
import com.vlasevsky.gym.model.Role;
import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.User;
import com.vlasevsky.gym.repository.UserRepository;
import com.vlasevsky.gym.security.config.JwtService;
import com.vlasevsky.gym.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Service
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceMap implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final ReportSenderService reportSenderService;

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

    @Override
    @Transactional
    public RegistrationResponse registerTrainee(TraineeRegistrationDto request) {
        log.info("Registering trainee: {}", request);
        Trainee user = new Trainee();
        String password = generateRandomPassword();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(generateUsername(request.firstName(), request.lastName()));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        log.info("Trainee registered successfully: {}", user.getUsername());
        reportSenderService.sendReport("Trainee registered successfully: " + user.getUsername());
        return RegistrationResponse.builder()
                .token(token)
                .password(password)
                .build();
    }

    @Override
    @Transactional
    public RegistrationResponse registerTrainer(TrainerRegistrationDto request) {
        log.info("Registering trainer: {}", request);
        Trainer user = new Trainer();
        String password = generateRandomPassword();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(generateUsername(request.firstName(), request.lastName()));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        log.info("Trainer registered successfully: {}", user.getUsername());
        reportSenderService.sendReport("Trainer registered successfully: " + user.getUsername());
        return RegistrationResponse.builder()
                .token(token)
                .password(password)
                .build();
    }

    @Override
    public void logout(String token) {
        jwtService.invalidateToken(token);
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            User user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> {
                        log.error("User not found: {}", request.username());
                        return new UserNotFoundException(request.username());
                    });
            log.info("Authenticating user: {}", request.username());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            String token = jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", request.username());
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new com.vlasevsky.gym.exceptions.AuthenticationException(e.getMessage());
        }
    }
}
