package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.service.AuthenticationService;
import com.vlasevsky.gym.service.TrainerService;
import com.vlasevsky.gym.service.map.ReportSenderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/trainers")
@AllArgsConstructor
public class TrainerController {

    @Autowired
    private TrainerService trainerService;


    private AuthenticationService authenticationService;

    @GetMapping
    public Set<TrainerReadDto> getTrainers(@RequestParam(required = false) String unassignedTraineeUsername) {
        if (unassignedTraineeUsername != null) {
            return trainerService.getTrainersNotAssignedToTrainee(unassignedTraineeUsername);
        }
        return trainerService.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerTrainer(
            @RequestBody TrainerRegistrationDto request
    ) {
        return ResponseEntity.ok(authenticationService.registerTrainer(request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileReadDto> getTrainerProfile(@PathVariable String username) {
        TrainerProfileReadDto profile = trainerService.findTrainerByUsername(username);
        if (profile != null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileReadDto> updateTrainerProfile(@PathVariable String username, @RequestBody TrainerCreateDto dto) {
        TrainerProfileReadDto updatedProfile = trainerService.update(username, dto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Void> changeTrainerActiveStatus(@PathVariable String username, @RequestBody StatusUpdateDto dto) {
        trainerService.changeActiveStatus(username, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/workload")
    public ResponseEntity<Void> updateWorkload(@RequestBody TrainerWorkloadRequest request) {
        trainerService.updateTrainerWorkload(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workload/{username}")
    public ResponseEntity<TrainerWorkloadSummary> getWorkload(@PathVariable String username,
                                                              @RequestParam int year,
                                                              @RequestParam int month) {
        TrainerWorkloadSummary summary = trainerService.getTrainerWorkload(username, year, month);
        return ResponseEntity.ok(summary);
    }

}
