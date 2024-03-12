package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.storage.Storage;
import org.vlasevsky.gym.storage.TraineeStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDAOImplTest {

    @Mock
    private Storage<Trainee> mockStorage;

    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    @BeforeEach
    void setUp() {
        mockStorage = mock(TraineeStorage.class);
        traineeDAO = new TraineeDAOImpl();
        traineeDAO.storage = mockStorage;
    }


    @Test
    void findByIdShouldReturnTraineeWhenExists() {
        // Given
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setId(1L);
        when(mockStorage.getAllData()).thenReturn(Map.of(1L, expectedTrainee));

        // When
        Optional<Trainee> result = traineeDAO.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedTrainee, result.get());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotExists() {
        // Given
        when(mockStorage.getAllData()).thenReturn(Map.of());

        // When
        Optional<Trainee> result = traineeDAO.findById(1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void saveShouldAddTraineeWhenNew() {
        // Given
        Trainee newTrainee = new Trainee();
        newTrainee.setDateOfBirth(new Date());
        newTrainee.setAddress("123 Main St");
        when(mockStorage.getAllData()).thenReturn(new HashMap<>());

        // When
        Trainee savedTrainee = traineeDAO.save(newTrainee);

        // Then
        assertNotNull(savedTrainee.getId());
        verify(mockStorage, times(2)).getAllData();
    }

    @Test
    void deleteShouldRemoveTraineeWhenExists() {
        // Given
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(1L);
        Map<Long, Trainee> storageMap = new HashMap<>();
        storageMap.put(1L, existingTrainee);
        when(mockStorage.getAllData()).thenReturn(storageMap);

        // When
        traineeDAO.delete(existingTrainee);

        // Then
        assertFalse(storageMap.containsKey(1L));
    }
}