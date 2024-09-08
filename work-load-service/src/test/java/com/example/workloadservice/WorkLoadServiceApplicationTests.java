package com.example.workloadservice.service;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.exception.TrainerWorkloadNotFoundException;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.model.YearlyTrainingSummary;
import com.example.workloadservice.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceMapTest {

	@InjectMocks
	private TrainerWorkloadServiceMap trainerWorkloadServiceMap;

	@Mock
	private TrainerWorkloadRepository repository;

	@Test
	void testUpdateWorkload_AddNewWorkload() {
		TrainerWorkloadRequest request = createWorkloadRequest(TrainerWorkloadRequest.ActionType.ADD, 5);

		// Mock поведения репозитория
		when(repository.findByUsername(anyString())).thenReturn(Optional.empty());

		trainerWorkloadServiceMap.updateWorkload(request);

		// Проверка, что вызван метод сохранения новой записи
		verify(repository, times(1)).save(any(TrainerWorkloadSummary.class));
	}

	@Test
	void testGetWorkload_NotFound() {
		when(repository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(TrainerWorkloadNotFoundException.class, () -> {
			trainerWorkloadServiceMap.getWorkload("nonexistent.user", 2024, 6);
		});
	}

	@Test
	void testGetWorkload_Success() {
		TrainerWorkloadSummary existingSummary = createExistingTrainerSummary(null);
		when(repository.findByUsername(anyString())).thenReturn(Optional.of(existingSummary));

		TrainerWorkloadSummary result = trainerWorkloadServiceMap.getWorkload("existing.user", 2024, 6);

		assertNotNull(result);
		assertEquals("existing.user", result.getUsername());
		assertTrue(result.getYearlySummaries().get(0).getMonthlySummary().containsKey(6));
	}

	// Вспомогательные методы для создания данных

	private TrainerWorkloadRequest createWorkloadRequest(TrainerWorkloadRequest.ActionType actionType, int duration) {
		TrainerWorkloadRequest request = new TrainerWorkloadRequest();
		request.setTransactionId("12345");
		request.setUsername("test.user");
		request.setFirstName("Test");
		request.setLastName("User");
		request.setIsActive(true);
		request.setTrainingDate(LocalDate.of(2024, 6, 1));  // Июнь 2024
		request.setTrainingDuration(duration);
		request.setActionType(actionType);
		return request;
	}

	private TrainerWorkloadSummary createExistingTrainerSummary(TrainerWorkloadRequest request) {
		TrainerWorkloadSummary summary = new TrainerWorkloadSummary();
		summary.setUsername(request != null ? request.getUsername() : "existing.user");
		summary.setFirstName("Existing");
		summary.setLastName("User");
		summary.setIsActive(true);

		YearlyTrainingSummary yearlySummary = new YearlyTrainingSummary();
		yearlySummary.setTrainingYear(2024);
		yearlySummary.setMonthlySummary(new HashMap<>());
		yearlySummary.getMonthlySummary().put(6, 5);  // Добавляем данные для июня 2024

		summary.setYearlySummaries(new ArrayList<>());
		summary.getYearlySummaries().add(yearlySummary);  // Добавляем годовую сводку в список yearlySummaries
		return summary;
	}

}
