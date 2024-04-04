package org.vlasevsky.gym.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.mapper.mapstruct.TraineeMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainerMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainingMapper;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceMapTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private UserCredentialsService userCredentialsService;

    @InjectMocks
    private TraineeServiceMap traineeService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        TraineeCreateAndUpdateDto dto = new TraineeCreateAndUpdateDto("Boris", "Vlasevsky", "123 Main St", new Date(), true);
        Trainee trainee = new Trainee();
        trainee.setFirstName("Boris");
        trainee.setLastName("Vlasevsky");

        when(traineeMapper.toEntity(dto)).thenReturn(trainee);
        when(userCredentialsService.generateUsername("Boris", "Vlasevsky")).thenReturn("boris.vlasevsky");
        when(userCredentialsService.generateRandomPassword()).thenReturn("password");
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        CredentialsDto credentials = traineeService.create(dto);

        assertNotNull(credentials);
        assertEquals("boris.vlasevsky", credentials.getUsername());
        assertEquals("password", credentials.getPassword());

        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testFindTraineeByUsername() {
        String username = "boris.vlasevsky";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeMapper.toDto(trainee)).thenReturn(new TraineeReadDto(1L, "Boris Vlasevsky"));

        TraineeReadDto result = traineeService.findTraineeByUsername(username, new CredentialsDto(username, "password"));

        assertNotNull(result);
        assertEquals("Boris Vlasevsky", result.name());

        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void testActivateTrainee() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setIsActive(false);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).update(trainee);

        traineeService.activateTrainee(traineeId, new CredentialsDto("boris.vlasevsky", "password"));

        assertTrue(trainee.getIsActive());
        verify(traineeRepository, times(1)).update(trainee);
    }

    @Test
    void testDeactivateTrainee() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setIsActive(true);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).update(trainee);

        traineeService.deactivateTrainee(traineeId, new CredentialsDto("boris.vlasevsky", "password"));

        assertFalse(trainee.getIsActive());
        verify(traineeRepository, times(1)).update(trainee);
    }

    @Test
    void testDeleteTraineeByUsername() {
        String username = "boris.vlasevsky";
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername(username);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee.getId());

        traineeService.deleteTraineeByUsername(username, new CredentialsDto(username, "password"));

        verify(traineeRepository, times(1)).delete(trainee.getId());
    }

    @Test
    void testChangeTraineePassword() {
        Long traineeId = 1L;
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setPassword("oldPassword");

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        doNothing().when(userCredentialsService).changePassword(traineeId, newPassword);

        traineeService.changeTraineePassword(traineeId, newPassword, new CredentialsDto("boris.vlasevsky", "password"));

        verify(userCredentialsService, times(1)).changePassword(traineeId, newPassword);
    }

    @Test
    void testDelete() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(traineeId);

        traineeService.delete(trainee, new CredentialsDto("boris.vlasevsky", "password"));

        verify(traineeRepository, times(1)).delete(traineeId);
    }

    @Test
    void testGetTraineeTrainings() {
        String username = "boris.vlasevsky";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "John Doe";
        String trainingType = "CARDIO";
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        trainings.add(training);

        when(traineeRepository.findTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(trainings);
        when(trainingMapper.toDTOList(trainings)).thenReturn(Collections.singletonList(new TrainingReadDto(1L, "Training 1", new Date(), 60, null, null)));

        List<TrainingReadDto> result = traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType, new CredentialsDto("boris.vlasevsky", "password"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());

        verify(traineeRepository, times(1)).findTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType);
    }

    @Test
    void testUpdateTraineeTrainers() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        Set<Trainer> trainers = new HashSet<>();
        Trainer trainer = new Trainer();
        trainers.add(trainer);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).update(trainee);

        traineeService.updateTraineeTrainers(traineeId, trainers, new CredentialsDto("boris.vlasevsky", "password"));

        assertEquals(trainers, trainee.getTrainers());
        verify(traineeRepository, times(1)).update(trainee);
    }

    @Test
    void testGetTrainersNotAssignedToTrainee() {
        String traineeUsername = "boris.vlasevsky";
        List<Trainer> trainers = new ArrayList<>();
        Trainer trainer = new Trainer();
        trainers.add(trainer);

        when(trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername)).thenReturn(trainers);
        when(trainerMapper.toDTOList(trainers)).thenReturn(Collections.singletonList(new TrainerReadDto(1L, "Trainer 1")));

        List<TrainerReadDto> result = traineeService.getTrainersNotAssignedToTrainee(traineeUsername, new CredentialsDto("boris.vlasevsky", "password"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());

        verify(trainerRepository, times(1)).findTrainersNotAssignedToTrainee(traineeUsername);
    }
}