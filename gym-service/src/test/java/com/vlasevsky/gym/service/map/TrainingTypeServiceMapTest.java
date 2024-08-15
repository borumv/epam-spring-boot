package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceMapTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceMap trainingTypeService;

    private static final List<TrainingType> TRAINING_TYPE_LIST = Collections.singletonList(new TrainingType());

    @Test
    void testFindAll() {
        when(trainingTypeRepository.findAll()).thenReturn(TRAINING_TYPE_LIST);

        List<TrainingType> result = trainingTypeService.findAll();

        assertEquals(TRAINING_TYPE_LIST, result);
    }
}
