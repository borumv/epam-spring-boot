package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.storage.Storage;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class TrainingDAOImplTest {

    private Storage<Training> storage;

    private TrainingDAOImpl trainingDAO;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        trainingDAO = new TrainingDAOImpl();
        trainingDAO.storage = storage;
    }

    @Test
    void findByIdShouldReturnTraining() {
        Training expectedTraining = new Training();
        expectedTraining.setId(1L);

        when(storage.findById(anyLong())).thenReturn(Optional.of(expectedTraining));

        Optional<Training> actualTraining = trainingDAO.findById(1L);

        verify(storage, times(1)).findById(1L);
        assert (actualTraining.isPresent());
        assert (actualTraining.get().getId().equals(expectedTraining.getId()));
    }

    @Test
    void saveShouldPersistTraining() {
        Training trainingToSave = new Training();
        trainingToSave.setId(1L);

        when(storage.save(any(Training.class))).thenReturn(trainingToSave);

        Training savedTraining = trainingDAO.save(trainingToSave);

        verify(storage, times(1)).save(trainingToSave);
        assert (savedTraining.getId().equals(trainingToSave.getId()));
    }

}