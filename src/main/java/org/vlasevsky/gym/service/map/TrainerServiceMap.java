package org.vlasevsky.gym.service.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TrainerCreateDto;
import org.vlasevsky.gym.dto.TrainerReadDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TrainerNotFoundException;
import org.vlasevsky.gym.exceptions.UserNotFoundException;
import org.vlasevsky.gym.mapper.Mapper;
import org.vlasevsky.gym.mapper.TrainerCreateMapper;
import org.vlasevsky.gym.mapper.TrainerReadMapper;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.service.TrainerService;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceMap implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainerCreateMapper userCreateMapper;
    private final TrainerReadMapper trainerReadMapper;
    private final UserCredentialsService userCredentialsService;

    public <T> Optional<T> findById(Long id, Mapper<Trainer, T> mapper, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        return trainerRepository.findById(id)
                .map(mapper::mapFrom);
    }

    public TrainerReadDto findById(Long id, CredentialsDto credentialsDto) {
        log.info("Finding trainer by ID: {}", id);
        return findById(id, trainerReadMapper, credentialsDto).orElseThrow(() -> new TrainerNotFoundException(id));
    }

    public CredentialsDto create(TrainerCreateDto trainer) {
        Trainer trainerEntity = userCreateMapper.mapFrom(trainer);
        String username = userCredentialsService.generateUsername(trainer.firstName(), trainer.lastName());
        String password = userCredentialsService.generateRandomPassword();
        trainerEntity.setUsername(username);
        trainerEntity.setPassword(password);
        trainerRepository.save(trainerEntity);

        return new CredentialsDto(username, password);
    }

    public void changeTrainerPassword(Long trainerId, String newPassword, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Checking password for trainer with trainer id: {}", trainerId);
        userCredentialsService.changePassword(trainerId, newPassword);
    }

    public boolean checkTrainerCredentials(String username, String password, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Checking credentials for trainer with username: {}", username);
        return userRepository.checkCredentials(username, password);
    }

    public TrainerReadDto findTrainerByUsername(String username, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Finding trainer by username: {}", username);
        return trainerRepository.findByUsername(username)
                .map(trainerReadMapper::mapFrom)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void activateTrainer(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException(id));
        if (!trainer.getIsActive()) { // Проверка, что тренер не активирован
            trainer.setIsActive(true);
            trainerRepository.update(trainer);
        }
    }

    public void deactivateTrainer(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException(id));
        if (trainer.getIsActive()) { // Проверка, что тренер активирован
            trainer.setIsActive(false);
            trainerRepository.update(trainer);
        }
    }
}
