package com.example.workloadservice.controller;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.service.TrainerWorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workload")
@Slf4j
public class TrainerWorkloadController {

    @Autowired
    private TrainerWorkloadService workloadService;

    @Transactional
    @PostMapping
    public ResponseEntity<Void> updateWorkload(@RequestBody TrainerWorkloadRequest request) {
        workloadService.updateWorkload(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerWorkloadSummary> getWorkload(@PathVariable String username,
                                                              @RequestParam int year,
                                                              @RequestParam int month) {
        TrainerWorkloadSummary summary = workloadService.getWorkload(username, year, month);
        return ResponseEntity.ok(summary);
    }
}
