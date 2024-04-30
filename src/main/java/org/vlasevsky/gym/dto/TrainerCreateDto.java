package org.vlasevsky.gym.dto;

import jakarta.validation.constraints.NotBlank;
import org.vlasevsky.gym.model.TrainingType;

import java.util.List;

public record TrainerCreateDto(
        @NotBlank(message = "First name must not be blank")
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        String lastName,
        List<TrainingType.Type> specializations,
        Boolean isActive) {
}
