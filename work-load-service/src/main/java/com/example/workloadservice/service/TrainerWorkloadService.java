package com.example.workloadservice.service;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.exception.TrainerWorkloadNotFoundException;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.model.YearlyTrainingSummary;
import com.example.workloadservice.repository.TrainerWorkloadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TrainerWorkloadService {

    @Autowired
    private TrainerWorkloadRepository repository;

    public void updateWorkload(TrainerWorkloadRequest request) {
        log.info("Updating workload for trainer: {}", request.getUsername());

        TrainerWorkloadSummary summary = repository.findByUsername(request.getUsername());

        if (summary == null) {
            log.info("No existing summary found for trainer: {}. Creating new summary.", request.getUsername());
            summary = new TrainerWorkloadSummary();
            summary.setUsername(request.getUsername());
            summary.setFirstName(request.getFirstName());
            summary.setLastName(request.getLastName());
            summary.setIsActive(request.getIsActive());
            summary.setYearlySummaries(new ArrayList<>());
        }

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();
        log.debug("Processing training for year: {}, month: {}", year, month);

        YearlyTrainingSummary yearlySummary = summary.getYearlySummaries().stream()
                .filter(ys -> ys.getTrainingYear().equals(year))
                .findFirst()
                .orElse(null);

        if (yearlySummary == null) {
            log.debug("No yearly summary found for year: {}. Creating new yearly summary.", year);
            yearlySummary = new YearlyTrainingSummary();
            yearlySummary.setTrainingYear(year);
            yearlySummary.setMonthlySummary(new HashMap<>());
            summary.getYearlySummaries().add(yearlySummary);
        }

        Map<Integer, Integer> monthlySummary = yearlySummary.getMonthlySummary();

        if (request.getActionType() == TrainerWorkloadRequest.ActionType.DELETE) {
            if (!monthlySummary.containsKey(month)) {
                log.warn("No monthly summary found for month: {} in year: {}. Cannot delete workload for non-existing month.", month, year);
                return;
            }

            int currentDuration = monthlySummary.get(month);
            int newDuration = currentDuration - request.getTrainingDuration();

            if (newDuration > 0) {
                monthlySummary.put(month, newDuration);
                log.info("Updated workload for trainer: {} for year: {}, month: {}. New duration: {}", request.getUsername(), year, month, newDuration);
            } else {
                monthlySummary.remove(month);
                log.info("Removed workload for trainer: {} for year: {}, month: {} as the duration reached zero.", request.getUsername(), year, month);
            }

            if (monthlySummary.isEmpty()) {
                summary.getYearlySummaries().remove(yearlySummary);
                log.info("Removed yearly summary for trainer: {} for year: {} as no monthly data remains.", request.getUsername(), year);
            }

            repository.save(summary);
        } else {
            monthlySummary.merge(month, request.getTrainingDuration(), Integer::sum);
            log.info("Saving updated workload summary for trainer: {}", request.getUsername());
            repository.save(summary);
        }
    }

    public TrainerWorkloadSummary getAllWorkload(String username, int year, int month) {
        log.info("Retrieving workload summary for trainer: {} for year: {} and month: {}", username, year, month);
        TrainerWorkloadSummary summary = repository.findByUsername(username);

        if (summary != null) {
            Optional<YearlyTrainingSummary> yearlySummary = summary.getYearlySummaries().stream()
                    .filter(ys -> ys.getTrainingYear().equals(year))
                    .findFirst();

            if (yearlySummary.isPresent() && yearlySummary.get().getMonthlySummary().containsKey(month)) {
                log.info("Workload summary found for trainer: {} for year: {} and month: {}", username, year, month);
                return summary;
            } else {
                log.warn("No workload summary found for trainer: {} for year: {} and month: {}", username, year, month);
            }
        } else {
            log.warn("No workload summary found for trainer: {}", username);

        }
        return new TrainerWorkloadSummary();
    }
    public TrainerWorkloadSummary getWorkload(String username, int year, int month) {
        log.info("Retrieving workload summary for trainer: {} for year: {} and month: {}", username, year, month);
        TrainerWorkloadSummary summary = repository.findByUsername(username);

        if (summary != null) {
            Optional<YearlyTrainingSummary> yearlySummaryOpt = summary.getYearlySummaries().stream()
                    .filter(ys -> ys.getTrainingYear().equals(year))
                    .findFirst();

            if (yearlySummaryOpt.isPresent()) {
                YearlyTrainingSummary yearlySummary = yearlySummaryOpt.get();
                if (yearlySummary.getMonthlySummary().containsKey(month)) {
                    log.info("Workload summary found for trainer: {} for year: {} and month: {}", username, year, month);

                    TrainerWorkloadSummary filteredSummary = new TrainerWorkloadSummary();
                    filteredSummary.setId(summary.getId());
                    filteredSummary.setUsername(summary.getUsername());
                    filteredSummary.setFirstName(summary.getFirstName());
                    filteredSummary.setLastName(summary.getLastName());
                    filteredSummary.setIsActive(summary.getIsActive());

                    YearlyTrainingSummary filteredYearlySummary = new YearlyTrainingSummary();
                    filteredYearlySummary.setId(yearlySummary.getId());
                    filteredYearlySummary.setTrainingYear(year);
                    filteredYearlySummary.setMonthlySummary(Collections.singletonMap(month, yearlySummary.getMonthlySummary().get(month)));

                    filteredSummary.setYearlySummaries(Collections.singletonList(filteredYearlySummary));

                    return filteredSummary;
                } else {
                    log.warn("No workload summary found for trainer: {} for year: {} and month: {}", username, year, month);
                }
            } else {
                log.warn("No yearly summary found for trainer: {} for year: {}", username, year);
            }
        } else {
            log.warn("No workload summary found for trainer: {}", username);
        }

        return new TrainerWorkloadSummary();
    }
}
