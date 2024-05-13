package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.service.TraineeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trainees")
@AllArgsConstructor
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<CredentialsDto> registerTrainee(@RequestBody TraineeRegistrationDto registrationDto) {
        CredentialsDto credentials = traineeService.register(registrationDto);
        return new ResponseEntity<>(credentials, HttpStatus.CREATED);
    }

    @GetMapping()
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileReadDto> getTraineeProfile(@PathVariable String username) {
        TraineeProfileReadDto profile = traineeService.findTraineeByUsername(username);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileReadDto> updateTraineeProfile(@PathVariable String username, @RequestBody TraineeCreateAndUpdateDto dto) {
        TraineeProfileReadDto updatedProfile = traineeService.update(username, dto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        traineeService.delete(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Void> changeTraineeActiveStatus(@PathVariable String username, @RequestBody StatusUpdateDto statusDto) {
        traineeService.changeActiveStatus(username, statusDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<TraineeProfileReadDto> updateTraineeTrainersList(@PathVariable String username, @RequestParam List<String> trainers) {
        TraineeProfileReadDto traineeProfileReadDto = traineeService.updateTraineeTrainers(username, trainers);
        return new ResponseEntity<>(traineeProfileReadDto, HttpStatus.OK);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingReadDto>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String trainerName) {
        List<TrainingReadDto> trainings = traineeService.getTraineeTrainings(username, from, to, trainerName);
        return ResponseEntity.ok(trainings);
    }

}
