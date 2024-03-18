package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.storage.Storage;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TrainerDAOImplTest {

    private Storage<Trainer> storage;


    private TrainerDAOImpl trainerDAO;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        trainerDAO = new TrainerDAOImpl();
        trainerDAO.storage = storage;
    }

    @Test
    void findByIdShouldReturnTrainer() {
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setId(1L);

        when(storage.findById(anyLong())).thenReturn(Optional.of(expectedTrainer));

        Optional<Trainer> actualTrainer = trainerDAO.findById(1L);

        verify(storage, times(1)).findById(1L);
        assert (actualTrainer.isPresent());
        assert (actualTrainer.get().getId().equals(expectedTrainer.getId()));
    }

    @Test
    void saveShouldPersistTrainer() {
        Trainer trainerToSave = new Trainer();
        trainerToSave.setId(1L);

        when(storage.save(any(Trainer.class))).thenReturn(trainerToSave);

        Trainer savedTrainer = trainerDAO.save(trainerToSave);

        verify(storage, times(1)).save(trainerToSave);
        assert (savedTrainer.getId().equals(trainerToSave.getId()));
    }

}