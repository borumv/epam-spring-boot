package org.vlasevsky.gym.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vlasevsky.gym.dto.TrainingCreateDto;
import org.vlasevsky.gym.service.TrainingService;

@RestController
@RequestMapping("/api/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @PostMapping
    public ResponseEntity<Void> registerTrainee(@RequestBody TrainingCreateDto createDto) {
        trainingService.create(createDto);
        return ResponseEntity.ok().build();
    }

}
