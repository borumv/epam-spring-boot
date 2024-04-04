package org.vlasevsky.gym.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TrainerCreateDto;
import org.vlasevsky.gym.dto.TrainerReadDto;
import org.vlasevsky.gym.mapper.mapstruct.TrainerMapper;
import org.vlasevsky.gym.model.Trainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceMapTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private UserCredentialsService userCredentialsService;

    @InjectMocks
    private TrainerServiceMap trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        TrainerCreateDto dto = new TrainerCreateDto("Boris", "Vlasevsky", true);
        Trainer trainer = new Trainer();
        trainer.setFirstName("Boris");
        trainer.setLastName("Vlasevsky");

        when(trainerMapper.toEntity(dto)).thenReturn(trainer);
        when(userCredentialsService.generateUsername("Boris", "Vlasevsky")).thenReturn("boris.vlasevsky");
        when(userCredentialsService.generateRandomPassword()).thenReturn("password");
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        CredentialsDto credentials = trainerService.create(dto);

        assertNotNull(credentials);
        assertEquals("boris.vlasevsky", credentials.getUsername());
        assertEquals("password", credentials.getPassword());

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testFindTrainerByUsername() {
        String username = "boris.vlasevsky";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);

        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(new TrainerReadDto(1L, "Boris Vlasevsky"));

        TrainerReadDto result = trainerService.findTrainerByUsername(username, new CredentialsDto(username, "password"));

        assertNotNull(result);
        assertEquals("Boris Vlasevsky", result.name());

        verify(trainerRepository, times(1)).findByUsername(username);
    }

    @Test
    void testChangeTrainerPassword() {
        Long trainerId = 1L;
        String newPassword = "newPassword";
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");

        when(userCredentialsService.checkCredentials(credentialsDto)).thenReturn(false);

        trainerService.changeTrainerPassword(trainerId, newPassword, credentialsDto);

        verify(userCredentialsService, times(1)).changePassword(trainerId, newPassword);
    }

    @Test
    void testActivateTrainer() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setIsActive(false);
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(userCredentialsService.checkCredentials(credentialsDto)).thenReturn(false);

        trainerService.activateTrainer(trainerId, credentialsDto);

        assertTrue(trainer.getIsActive());
        verify(trainerRepository, times(1)).update(trainer);
    }

    @Test
    void testDeactivateTrainer() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        trainer.setIsActive(true);
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(userCredentialsService.checkCredentials(credentialsDto)).thenReturn(false);

        trainerService.deactivateTrainer(trainerId, credentialsDto);

        assertFalse(trainer.getIsActive());
        verify(trainerRepository, times(1)).update(trainer);
    }

    @Test
    void testFindById() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(trainerMapper.toDto(trainer)).thenReturn(new TrainerReadDto(trainerId, "Boris Vlasevsky"));

        TrainerReadDto result = trainerService.findById(trainerId, credentialsDto);

        assertNotNull(result);
        assertEquals(trainerId, result.id());
        assertEquals("Boris Vlasevsky", result.name());

        verify(trainerRepository, times(1)).findById(trainerId);
    }
}
