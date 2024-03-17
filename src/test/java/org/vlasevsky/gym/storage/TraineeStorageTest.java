package org.vlasevsky.gym.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vlasevsky.gym.model.StorageData;
import org.vlasevsky.gym.model.Trainee;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeStorageTest {

    @Mock
    private StorageDataService storageDataService;

    private TraineeStorage traineeStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("Boris");
        trainee.setLastName("Vlasevsky");
        Trainee[] trainees = {trainee};
        StorageData storageData = new StorageData();
        storageData.setTrainees(trainees);
        when(storageDataService.getStorageData()).thenReturn(storageData);

        traineeStorage = new TraineeStorage(storageDataService); // Manually create the TraineeStorage instance
    }

    @Test
    void testGetAllData() {
        Map<Long, Trainee> allData = traineeStorage.getAllData();
        assertEquals(1, allData.size());
        assertEquals("Boris", allData.get(1L).getFirstName());
        assertEquals("Vlasevsky", allData.get(1L).getLastName());
    }
}