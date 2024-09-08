package com.example.workloadservice.controller;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.dto.TrainerWorkloadSummaryDTO;
import com.example.workloadservice.mapstruct.TrainerWorkloadSummaryMapper;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workload")
@Slf4j
@Validated
public class TrainerWorkloadController {

    @Autowired
    private TrainerWorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Void> updateWorkload(@RequestBody @Valid TrainerWorkloadRequest request) {
        log.info("TransactionId: {} - Received workload update request for trainer: {}", request.getTransactionId(), request.getUsername());
        workloadService.updateWorkload(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerWorkloadSummaryDTO> getWorkload(@PathVariable String username,
                                                                 @RequestParam int year,
                                                                 @RequestParam int month) {
        TrainerWorkloadSummary summary = workloadService.getWorkload(username, year, month);
        log.info("WorkLoad Summary  - {}", summary);
        TrainerWorkloadSummaryDTO summaryDTO = TrainerWorkloadSummaryMapper.INSTANCE.toDTO(summary);
        log.info("Summary  - {}", summaryDTO);

        return ResponseEntity.ok(summaryDTO);
    }
}
