package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.model.Trainer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TrainerRepositoryTest {

    @Mock
    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Long trainerId = 1L;
        Trainer expectedTrainer = new Trainer();
        expectedTrainer.setId(trainerId);
        expectedTrainer.setFirstName("Boris");
        expectedTrainer.setLastName("Vlasevsky");
        expectedTrainer.setIsActive(true);

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(expectedTrainer));

        Optional<Trainer> actualTrainer = trainerRepository.findById(trainerId);

        assertTrue(actualTrainer.isPresent());
        assertEquals(expectedTrainer, actualTrainer.get());

        verify(trainerRepository, times(1)).findById(trainerId);
    }

    @Test
    void testSave() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Boris");
        trainer.setLastName("Vlasevsky");
        trainer.setIsActive(true);

        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer savedTrainer = trainerRepository.save(trainer);

        assertEquals(trainer, savedTrainer);

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testFindAll() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setFirstName("Boris");
        trainer1.setLastName("Vlasevsky");
        trainer1.setIsActive(true);

        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setFirstName("John");
        trainer2.setLastName("Doe");
        trainer2.setIsActive(true);

        List<Trainer> expectedTrainers = Arrays.asList(trainer1, trainer2);

        when(trainerRepository.findAll()).thenReturn(expectedTrainers);

        List<Trainer> actualTrainers = trainerRepository.findAll();

        assertEquals(expectedTrainers, actualTrainers);

        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        Long trainerId = 1L;

        doNothing().when(trainerRepository).delete(trainerId);

        trainerRepository.delete(trainerId);

        verify(trainerRepository, times(1)).delete(trainerId);
    }

}
