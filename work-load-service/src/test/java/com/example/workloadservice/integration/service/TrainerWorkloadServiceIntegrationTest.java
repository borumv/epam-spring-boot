package com.example.workloadservice.integration.service;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.integration.IntegrationTestBase;
import com.example.workloadservice.integration.annotation.IT;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.repository.TrainerWorkloadRepository;
import com.example.workloadservice.service.TrainerWorkloadServiceMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IT
public class TrainerWorkloadServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TrainerWorkloadServiceMap trainerWorkloadService;

    @Autowired
    private TrainerWorkloadRepository trainerWorkloadRepository;

    private TrainerWorkloadRequest workloadRequest;

    @BeforeEach
    public void setup() {
        workloadRequest = new TrainerWorkloadRequest(
                "transaction123",
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.now(),
                60,
                TrainerWorkloadRequest.ActionType.ADD
        );
    }

    @Test
    public void testUpdateWorkload_trainerSavedInDatabase() {
        // Act
        trainerWorkloadService.updateWorkload(workloadRequest);

        // Assert
        TrainerWorkloadSummary workload = trainerWorkloadService.getWorkload("john.doe", LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        assertNotNull(workload);
        assertEquals("John", workload.getFirstName());
        assertEquals("Doe", workload.getLastName());
    }

    @Test
    public void testGetWorkload_success() {
        // Arrange: создаем сводку для тренера
        trainerWorkloadService.updateWorkload(workloadRequest);

        // Act
        TrainerWorkloadSummary workloadSummary = trainerWorkloadService.getWorkload("john.doe", LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        // Assert
        assertNotNull(workloadSummary);
        assertEquals("john.doe", workloadSummary.getUsername());
    }
}
