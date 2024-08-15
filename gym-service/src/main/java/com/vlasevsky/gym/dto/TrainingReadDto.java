package com.vlasevsky.gym.dto;

import java.time.LocalDate;

public record TrainingReadDto(
        Long id,
        String name,
        LocalDate date,
        int duration,
        TraineeReadDto trainee,
        TrainerReadDto trainer
) {
}
