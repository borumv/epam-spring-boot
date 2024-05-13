package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TrainingTypeServiceMapTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceMap trainingTypeServiceMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsAllTrainingTypes() {

        TrainingType cardioType = new TrainingType();
        cardioType.setId(1L);
        cardioType.setName(TrainingType.Type.CARDIO);

        TrainingType strengthType = new TrainingType();
        strengthType.setId(2L);
        strengthType.setName(TrainingType.Type.STRENGTH_TRAINING);

        List<TrainingType> trainingTypes = List.of(cardioType, strengthType);
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeServiceMap.findAll();

        assertEquals(2, result.size());
        assertEquals(cardioType, result.get(0));
        assertEquals(strengthType, result.get(1));
    }
}