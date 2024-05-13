package com.vlasevsky.gym.service.map;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceMapTest {

    @Mock
    private TrainerRepository trainerRepository;


    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserCredentialsService userCredentialsService;

    @Spy
    private TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    @InjectMocks
    private TrainerServiceMap trainerServiceMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrainer() {
        // Given
        TrainerRegistrationDto registrationDto = new TrainerRegistrationDto("John", "Doe", List.of(TrainingType.Type.CARDIO));
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("johndoe");
        trainer.setPassword("password");

        when(trainerMapper.toEntity(registrationDto)).thenReturn(trainer);
        when(userCredentialsService.generateUsername("John", "Doe")).thenReturn("johndoe");
        when(userCredentialsService.generateRandomPassword()).thenReturn("password");
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        CredentialsDto result = trainerServiceMap.register(registrationDto);

        assertEquals("johndoe", result.username());
        assertEquals("password", result.password());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void findTrainerByUsernameThatExists() {
        // Given
        String username = "testUser";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecializations(new ArrayList<>());
        trainer.setTrainees(new HashSet<>());
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(new TrainerReadDto(1L, "testUser", "John", "Doe", List.of(TrainingType.Type.CARDIO)));

        // When
        TrainerProfileReadDto result = trainerServiceMap.findTrainerByUsername(username);

        // Then
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        verify(trainerRepository, times(1)).findByUsername(username);
    }

    @Test
    void findTrainerByUsernameThatDoesNotExist() {
        // Given
        String username = "nonExistentUser";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerServiceMap.findTrainerByUsername(username));
    }

    @Test
    void updateTrainerThatExists() {
        // Given
        String username = "testUser";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setSpecializations(new ArrayList<>());
        trainer.setTrainees(new HashSet<>());
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        TrainerCreateDto dto = new TrainerCreateDto("John", "Doe", List.of(TrainingType.Type.CARDIO), true);

        when(trainingTypeRepository.findByNames(dto.specializations())).thenReturn(List.of(new TrainingType()));

        // When
        TrainerProfileReadDto result = trainerServiceMap.update(username, dto);

        // Then
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals(true, trainer.getIsActive());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void updateTrainerThatDoesNotExist() {
        // Given
        String username = "nonExistentUser";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
        TrainerCreateDto dto = new TrainerCreateDto("John", "Doe", List.of(TrainingType.Type.CARDIO), true);

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerServiceMap.update(username, dto));
    }

    @Test
    void findAllTrainers() {
        // Given
        Trainer trainer = new Trainer();
        trainer.setUsername("testUser");
        trainer.setSpecializations(new ArrayList<>());
        trainer.setTrainees(new HashSet<>());
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));
        when(trainerMapper.toDTOList(List.of(trainer))).thenReturn(List.of(new TrainerReadDto(1L, "johndoe", "John", "Doe", List.of(TrainingType.Type.CARDIO))));

        // When
        List<TrainerReadDto> result = trainerServiceMap.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).firstName());
        assertEquals("Doe", result.get(0).lastName());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void changeActiveStatusForExistingTrainer() {
        // Given
        String username = "testUser";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setSpecializations(new ArrayList<>());
        trainer.setTrainees(new HashSet<>());
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(false);

        // When
        trainerServiceMap.changeActiveStatus(username, statusUpdateDto);

        // Then
        assertEquals(false, trainer.getIsActive());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void changeActiveStatusForNonExistingTrainer() {
        // Given
        String username = "nonExistentUser";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(false);

        // When/Then
        assertThrows(TrainerNotFoundException.class, () -> trainerServiceMap.changeActiveStatus(username, statusUpdateDto));
    }


    @Test
    void getTrainersNotAssignedToTrainee() {
        // Given
        String traineeUsername = "testTrainee";
        Trainer trainer = new Trainer();
        trainer.setUsername("testUser");
        trainer.setSpecializations(new ArrayList<>());
        trainer.setTrainees(new HashSet<>());
        when(trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername)).thenReturn(List.of(trainer));
        when(trainerMapper.toDTOList(List.of(trainer))).thenReturn(List.of(new TrainerReadDto(1L, "johndoe", "John", "Doe", List.of(TrainingType.Type.CARDIO))));

        // When
        List<TrainerReadDto> result = trainerServiceMap.getTrainersNotAssignedToTrainee(traineeUsername);

        // Then
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).firstName());
        assertEquals("Doe", result.get(0).lastName());
        verify(trainerRepository, times(1)).findTrainersNotAssignedToTrainee(traineeUsername);
    }
}