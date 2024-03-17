package org.vlasevsky.gym.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vlasevsky.gym.exceptions.TrainingNotFoundException;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.map.TrainingServiceMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDAOImplTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceMap trainingServiceMap;

    @Test
    void testFindByIdSuccess() {
        Long id = 1L;
        Training expectedTraining = new Training();
        expectedTraining.setId(id);
        when(trainingDAO.findById(id)).thenReturn(Optional.of(expectedTraining));

        Training actualTraining = trainingServiceMap.findById(id);

        assertEquals(expectedTraining, actualTraining);
        verify(trainingDAO).findById(id);
    }

    @Test
    void testFindByIdThrowsNotFoundException() {
        Long nonExistentId = 1L;
        when(trainingDAO.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> {
            trainingServiceMap.findById(nonExistentId);
        });

        verify(trainingDAO).findById(nonExistentId);
    }

    @Test
    void testSave() {
        Training trainingToSave = new Training();
        trainingToSave.setId(1L);
        when(trainingDAO.save(trainingToSave)).thenReturn(trainingToSave);

        Training savedTraining = trainingServiceMap.save(trainingToSave);

        assertEquals(trainingToSave, savedTraining);
        verify(trainingDAO).save(trainingToSave);
    }
}