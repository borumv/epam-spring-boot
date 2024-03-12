package org.vlasevsky.gym.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.model.StorageData;
import org.vlasevsky.gym.model.Training;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TrainingStorageTest {

    @Mock
    private StorageDataService storageDataService;

    private TrainingStorage trainingStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Training training = new Training();
        training.setId(1L);
        training.setName("Vasya");
        Training[] trainings = {training};
        StorageData storageData = new StorageData();
        storageData.setTrainings(trainings);
        when(storageDataService.getStorageData()).thenReturn(storageData);

        trainingStorage = new TrainingStorage(storageDataService); // Manually create the TrainingStorage instance
    }

    @Test
    void testGetAllData() {
        Map<Long, Training> allData = trainingStorage.getAllData();
        assertEquals(1, allData.size());
        assertEquals("Vasya", allData.get(1L).getName());
    }
}