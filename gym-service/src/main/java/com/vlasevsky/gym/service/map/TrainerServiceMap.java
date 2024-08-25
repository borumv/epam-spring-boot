package com.vlasevsky.gym.service.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlasevsky.gym.config.JmsConstants;
import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.mapstruct.TraineeMapper;
import com.vlasevsky.gym.mapstruct.TrainerMapper;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import com.vlasevsky.gym.service.TrainerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceMap implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;

    private JmsTemplate jmsTemplate;
    @Transactional
    @Override
    public TrainerProfileReadDto findTrainerByUsername(String username) {
        log.info("Finding trainer by username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username).orElseThrow(() -> new TrainerNotFoundException(username));
        if (trainer != null) {
            List<TrainingType.Type> specializations = TrainerMapper.mapSpecializations(trainer.getSpecializations());
            List<TraineeReadDto> traineesList = (trainer.getTrainees() != null && !trainer.getTrainees().isEmpty())
                    ? trainer.getTrainees().stream()
                    .map(traineeMapper::toDto)
                    .collect(Collectors.toList())
                    : Collections.emptyList();
            TrainerProfileReadDto profile = new TrainerProfileReadDto(
                    trainer.getUsername(),
                    trainer.getFirstName(),
                    trainer.getLastName(),
                    specializations,
                    traineesList
            );
            log.info("Trainer found: {}", profile);
            return profile;
        } else {
            log.warn("Trainer not found with username: {}", username);
            return null;
        }
    }

    @Transactional
    @Override
    public TrainerProfileReadDto update(String username, TrainerCreateDto dto) {
        log.info("Updating trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException("Trainer not found with username: " + username);
                });

        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setIsActive(dto.isActive());
        List<TrainingType> specializations = trainingTypeRepository.findByNames(dto.specializations());
        trainer.setSpecializations(specializations);
        trainerRepository.save(trainer);
        log.info("Trainer with username: {} updated successfully", username);
        return findTrainerByUsername(username);
    }

    @Transactional
    @Override
    public List<TrainerReadDto> findAll() {
        log.info("Finding all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        List<TrainerReadDto> trainerDtos = trainerMapper.toDTOList(trainers);
        log.info("Found {} trainers", trainerDtos.size());
        return trainerDtos;
    }

    @Transactional
    @Override
    public void changeActiveStatus(String username, StatusUpdateDto dto) {
        log.info("Changing active status for trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException(username);
                });
        trainer.setIsActive(dto.isActive());
        trainerRepository.save(trainer);
        log.info("Trainer {} active status changed to {}", username, dto.isActive());
    }

    @Transactional
    @Override
    public List<TrainingReadDto> getTrainerTrainings(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        log.info("Fetching trainings for trainer with username: {} from {} to {}", trainerUsername, fromDate, toDate);
        List<Training> trainings = trainingRepository.findTrainingsByTrainerAndPeriodAndTrainee(trainerUsername, fromDate, toDate, traineeName);
        List<TrainingReadDto> trainingDtos = trainingMapper.toDTOList(trainings);
        log.info("Found {} trainings", trainingDtos.size());
        return trainingDtos;
    }

    @Transactional
    @Override
    public List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Finding trainers not assigned to trainee with username: {}", traineeUsername);
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        List<TrainerReadDto> trainerDtos = trainerMapper.toDTOList(trainers);
        log.info("Found {} trainers not assigned to trainee", trainerDtos.size());
        return trainerDtos;
    }

    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequest request) {
        String username = request.getUsername();
        trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException(username);
                });
        jmsTemplate.convertAndSend(JmsConstants.WORKLOAD_QUEUE, request);
    }

    @Override
    public TrainerWorkloadSummary getTrainerWorkload(String username, int year, int month) {
        Map<String, Object> requestPayload = createRequestPayload(username, year, month);
        MessageCreator messageCreator = createMessageCreator(requestPayload);

        jmsTemplate.send(JmsConstants.WORKLOAD_REQUEST_QUEUE, messageCreator);

        return receiveAndProcessResponse()
                .orElse(new TrainerWorkloadSummary());
    }

    private Map<String, Object> createRequestPayload(String username, int year, int month) {
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("username", username);
        requestPayload.put("year", year);
        requestPayload.put("month", month);
        return requestPayload;
    }

    private MessageCreator createMessageCreator(Map<String, Object> requestPayload) {
        return session -> {
            Message message = session.createObjectMessage((Serializable) requestPayload);
            message.setJMSReplyTo(new ActiveMQQueue(JmsConstants.WORKLOAD_RESPONSE_QUEUE));
            return message;
        };
    }

    private Optional<TrainerWorkloadSummary> receiveAndProcessResponse() {
        Message response = jmsTemplate.receive(JmsConstants.WORKLOAD_RESPONSE_QUEUE);

        if (response instanceof TextMessage) {
            try {
                String jsonSummary = ((TextMessage) response).getText();
                TrainerWorkloadSummary summary = new ObjectMapper().readValue(jsonSummary, TrainerWorkloadSummary.class);
                log.info("Received summary: {}", summary);
                return Optional.of(summary);
            } catch (JMSException | JsonProcessingException e) {
                log.error("Error processing response message", e);
            }
        }

        return Optional.empty();
    }
}