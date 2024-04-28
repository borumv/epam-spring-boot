package org.vlasevsky.gym.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.mapper.mapstruct.TraineeMapper;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.service.TraineeService;
import org.vlasevsky.gym.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
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

    @GetMapping("/profile")
    public ResponseEntity<TraineeProfileReadDto> getTraineeProfile(@RequestParam String username) {
        TraineeProfileReadDto profile = traineeService.findTraineeByUsername(username);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<TraineeProfileReadDto> updateTraineeProfile(@RequestBody TraineeCreateAndUpdateDto dto) {
        TraineeProfileReadDto updatedProfile = traineeService.update(dto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteTraineeProfile(@RequestParam String username) {
        traineeService.delete(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> changeTraineeActiveStatus(@RequestParam String username, @RequestParam boolean isActive) {
        traineeService.changeActiveStatus(username, isActive);
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

    @GetMapping("/{username}/trainers")
    public List<TrainerReadDto> getNotAssignedTrainers(@PathVariable String username) {
        return traineeService.getTrainersNotAssignedToTrainee(username);
    }
}
