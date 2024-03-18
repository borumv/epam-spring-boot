package org.vlasevsky.gym.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.vlasevsky.gym.model.StorageData;
import org.vlasevsky.gym.model.Trainee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class StorageImplTest {
    @Mock
    private Resource dataResource;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StorageImpl<Trainee> storage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storage = new StorageImpl<>(Trainee.class);
    }

    @Test
    void findByIdShouldReturnTrainee() {
        Trainee expectedTrainee = new Trainee();
        expectedTrainee.setId(1L);
        storage.save(expectedTrainee);

        Optional<Trainee> actualTrainee = storage.findById(1L);

        assertTrue(actualTrainee.isPresent());
        assertEquals(expectedTrainee.getId(), actualTrainee.get().getId());
    }

    @Test
    void saveShouldPersistTrainee() {
        Trainee traineeToSave = new Trainee();
        traineeToSave.setId(1L);

        Trainee savedTrainee = storage.save(traineeToSave);

        assertEquals(traineeToSave.getId(), savedTrainee.getId());
    }

    @Test
    void deleteShouldRemoveTrainee() {
        Trainee traineeToDelete = new Trainee();
        traineeToDelete.setId(1L);
        storage.save(traineeToDelete);

        storage.delete(1L);

        assertFalse(storage.findById(1L).isPresent());
    }

}