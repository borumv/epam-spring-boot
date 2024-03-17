package org.vlasevsky.gym.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.model.StorageData;
import org.vlasevsky.gym.model.Trainer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TrainerStorageTest {


    @Mock
    private StorageDataService storageDataService;

    private TrainerStorage trainerStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("Anna");
        trainer.setLastName("Ivanova");
        Trainer[] trainers = {trainer};
        StorageData storageData = new StorageData();
        storageData.setTrainers(trainers);
        when(storageDataService.getStorageData()).thenReturn(storageData);

        trainerStorage = new TrainerStorage(storageDataService); // Manually create the TrainerStorage instance
    }

    @Test
    void testGetAllData() {
        Map<Long, Trainer> allData = trainerStorage.getAllData();
        assertEquals(1, allData.size());
        assertEquals("Anna", allData.get(1L).getFirstName());
        assertEquals("Ivanova", allData.get(1L).getLastName());
    }
}