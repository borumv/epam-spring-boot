package com.example.workloadservice.service;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.exception.TrainerWorkloadNotFoundException;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.model.YearlyTrainingSummary;
import com.example.workloadservice.repository.TrainerWorkloadRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainerWorkloadServiceMap implements TrainerWorkloadService {

    @Autowired
    private TrainerWorkloadRepository repository;

    @Override
    public void updateWorkload(@Valid TrainerWorkloadRequest request) {
        log.info("TransactionId: {} - Updating workload for trainer: {}", request.getTransactionId(), request.getUsername());

        TrainerWorkloadSummary summary = getOrCreateTrainerSummary(request);

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();
        log.debug("TransactionId: {} - Processing training for year: {}, month: {}", request.getTransactionId(), year, month);

        YearlyTrainingSummary yearlySummary = getOrCreateYearlySummary(summary, year, request.getTransactionId());

        if (request.getActionType() == TrainerWorkloadRequest.ActionType.DELETE) {
            deleteMonthlySummary(request, summary, yearlySummary, month);
        } else {
            updateMonthlySummary(request, yearlySummary, month);
        }

        repository.save(summary);
        log.info("TransactionId: {} - Successfully saved workload summary for trainer: {}", request.getTransactionId(), request.getUsername());
    }

    @Override
    public TrainerWorkloadSummary getWorkload(String username, int year, int month) {
        log.info("Retrieving workload summary for trainer: {} for year: {} and month: {}", username, year, month);

        TrainerWorkloadSummary summary = repository.findByUsername(username)
                .orElseThrow(() -> new TrainerWorkloadNotFoundException(username));

        YearlyTrainingSummary yearlySummary = getYearlySummary(summary, username, year);

        // Возвращаем сводку за год или за конкретный месяц
        return (month == 0) ?
                createSummary(summary, yearlySummary, year, null) :
                createSummary(summary, yearlySummary, year, month);
    }

    private TrainerWorkloadSummary getOrCreateTrainerSummary(TrainerWorkloadRequest request) {
        TrainerWorkloadSummary summary = repository.findByUsername(request.getUsername()).orElse(null);

        if (summary == null) {
            log.info("TransactionId: {} - No existing summary found for trainer: {}. Creating new summary.", request.getTransactionId(), request.getUsername());
            summary = new TrainerWorkloadSummary();
            summary.setUsername(request.getUsername());
            summary.setFirstName(request.getFirstName());
            summary.setLastName(request.getLastName());
            summary.setIsActive(request.getIsActive());
            summary.setYearlySummaries(new ArrayList<>());
        }

        return summary;
    }

    private YearlyTrainingSummary getOrCreateYearlySummary(TrainerWorkloadSummary summary, int year, String transactionId) {
        return summary.getYearlySummaries().stream()
                .filter(ys -> ys.getTrainingYear().equals(year))
                .findFirst()
                .orElseGet(() -> {
                    log.debug("TransactionId: {} - No yearly summary found for year: {}. Creating new yearly summary.", transactionId, year);
                    YearlyTrainingSummary yearlySummary = new YearlyTrainingSummary();
                    yearlySummary.setTrainingYear(year);
                    yearlySummary.setMonthlySummary(new HashMap<>());
                    summary.getYearlySummaries().add(yearlySummary);
                    return yearlySummary;
                });
    }

    private void deleteMonthlySummary(TrainerWorkloadRequest request, TrainerWorkloadSummary summary, YearlyTrainingSummary yearlySummary, int month) {
        Map<Integer, Integer> monthlySummary = yearlySummary.getMonthlySummary();

        if (!monthlySummary.containsKey(month)) {
            log.warn("TransactionId: {} - No monthly summary found for month: {} in year: {}. Cannot delete workload for non-existing month.", request.getTransactionId(), month, yearlySummary.getTrainingYear());
            throw new TrainerWorkloadNotFoundException(request.getUsername());
        }

        int currentDuration = monthlySummary.get(month);
        int newDuration = currentDuration - request.getTrainingDuration();

        if (newDuration > 0) {
            monthlySummary.put(month, newDuration);
            log.info("TransactionId: {} - Updated workload for trainer: {} for year: {}, month: {}. New duration: {}", request.getTransactionId(), request.getUsername(), yearlySummary.getTrainingYear(), month, newDuration);
        } else {
            monthlySummary.remove(month);
            log.info("TransactionId: {} - Removed workload for trainer: {} for year: {}, month: {} as the duration reached zero.", request.getTransactionId(), request.getUsername(), yearlySummary.getTrainingYear(), month);
        }

        if (monthlySummary.isEmpty()) {
            summary.getYearlySummaries().remove(yearlySummary);
            log.info("TransactionId: {} - Removed yearly summary for trainer: {} for year: {} as no monthly data remains.", request.getTransactionId(), request.getUsername(), yearlySummary.getTrainingYear());
        }
    }

    private void updateMonthlySummary(TrainerWorkloadRequest request, YearlyTrainingSummary yearlySummary, int month) {
        Map<Integer, Integer> monthlySummary = yearlySummary.getMonthlySummary();
        monthlySummary.merge(month, request.getTrainingDuration(), Integer::sum);
        log.info("TransactionId: {} - Added training duration for trainer: {} for year: {}, month: {}. Duration added: {}", request.getTransactionId(), request.getUsername(), yearlySummary.getTrainingYear(), month, request.getTrainingDuration());
    }

    private YearlyTrainingSummary getYearlySummary(TrainerWorkloadSummary summary, String username, int year) {
        return summary.getYearlySummaries().stream()
                .filter(ys -> ys.getTrainingYear().equals(year))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("TransactionId: {} - No yearly summary found for trainer: {} for year: {}", username, year);
                    return new TrainerWorkloadNotFoundException(username);
                });
    }

    private TrainerWorkloadSummary createSummary(TrainerWorkloadSummary summary, YearlyTrainingSummary yearlySummary, int year, Integer month) {
        TrainerWorkloadSummary filteredSummary = createBaseSummary(summary);

        YearlyTrainingSummary filteredYearlySummary = new YearlyTrainingSummary();
        filteredYearlySummary.setTrainingYear(year);

        // Если month == null, то возвращаем все месячные сводки за год
        Map<Integer, Integer> filteredMonthlySummary = (month == null) ?
                yearlySummary.getMonthlySummary().entrySet().stream()
                        .filter(entry -> entry.getKey() != 0) // Убираем ключ "0", если он был добавлен
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) :
                filterMonthlySummary(yearlySummary, month);

        filteredYearlySummary.setMonthlySummary(filteredMonthlySummary);
        filteredSummary.setYearlySummaries(Collections.singletonList(filteredYearlySummary));

        log.info("Filter summary: {}", filteredSummary);
        return filteredSummary;
    }

    private Map<Integer, Integer> filterMonthlySummary(YearlyTrainingSummary yearlySummary, Integer month) {
        if (!yearlySummary.getMonthlySummary().containsKey(month)) {
            log.warn("No workload summary found for month: {}", month);
            throw new TrainerWorkloadNotFoundException("Month " + month + " not found");
        }

        Map<Integer, Integer> filteredMonthlySummary = new HashMap<>();
        filteredMonthlySummary.put(month, yearlySummary.getMonthlySummary().get(month));
        return filteredMonthlySummary;
    }

    private TrainerWorkloadSummary createBaseSummary(TrainerWorkloadSummary summary) {
        TrainerWorkloadSummary baseSummary = new TrainerWorkloadSummary();
        baseSummary.setId(summary.getId());
        baseSummary.setUsername(summary.getUsername());
        baseSummary.setFirstName(summary.getFirstName());
        baseSummary.setLastName(summary.getLastName());
        baseSummary.setIsActive(summary.getIsActive());
        return baseSummary;
    }
}
