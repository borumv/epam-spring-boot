package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.storage.Storage;
import org.vlasevsky.gym.storage.TraineeStorage;
import org.vlasevsky.gym.storage.TrainerStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDAOImplTest {

    @Mock
    private Storage<Trainer> mockStorage;

    @InjectMocks
    private TrainerDAOImpl trainerDAO;

    @BeforeEach
    void setUp() {
        mockStorage = mock(TrainerStorage.class);
        trainerDAO = new TrainerDAOImpl();
        trainerDAO.storage = mockStorage;
    }
    @Test
    void findByIdShouldReturnTrainerWhenExists() {
        // Given
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setId(1L);
        when(mockStorage.getAllData()).thenReturn(Map.of(1L, expectedTrainer));

        // When
        Optional<Trainer> result = trainerDAO.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedTrainer, result.get());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotExists() {
        // Given
        when(mockStorage.getAllData()).thenReturn(Map.of());

        // When
        Optional<Trainer> result = trainerDAO.findById(1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void saveShouldAddTrainerWhenNew() {
        // Given
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Boris");
        when(mockStorage.getAllData()).thenReturn(new HashMap<>());

        // When
        Trainer savedTrainer = trainerDAO.save(newTrainer);

        // Then
        assertNotNull(savedTrainer.getId());
        verify(mockStorage, times(2)).getAllData();
    }


}