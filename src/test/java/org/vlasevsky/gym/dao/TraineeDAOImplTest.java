package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.storage.Storage;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDAOImplTest {
    private Storage<Trainee> mockStorage;
    private TraineeDAOImpl traineeDAO;

    @BeforeEach
    void setUp() {
        mockStorage = mock(Storage.class);
        traineeDAO = new TraineeDAOImpl();
        traineeDAO.storage = mockStorage;
    }

    @Test
    void findByIdShouldReturnTrainee() {
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setId(1L);

        when(mockStorage.findById(anyLong())).thenReturn(Optional.of(expectedTrainee));

        Optional<Trainee> actualTrainee = traineeDAO.findById(1L);

        verify(mockStorage, times(1)).findById(1L);
        assert (actualTrainee.isPresent());
        assert (actualTrainee.get().getId().equals(expectedTrainee.getId()));
    }

    @Test
    void saveShouldPersistTrainee() {
        Trainee traineeToSave = new Trainee();
        traineeToSave.setId(1L);

        when(mockStorage.save(any(Trainee.class))).thenReturn(traineeToSave);

        Trainee savedTrainee = traineeDAO.save(traineeToSave);

        verify(mockStorage, times(1)).save(traineeToSave);
        assert (savedTrainee.getId().equals(traineeToSave.getId()));
    }

    @Test
    void deleteShouldRemoveTrainee() {
        Trainee traineeToDelete = new Trainee();
        traineeToDelete.setId(1L);

        doNothing().when(mockStorage).delete(anyLong());

        traineeDAO.delete(traineeToDelete);

        verify(mockStorage, times(1)).delete(traineeToDelete.getId());
    }
}