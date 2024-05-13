package com.vlasevsky.gym.dto;

import com.vlasevsky.gym.model.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TrainerRegistrationDto(
        @NotBlank(message = "First name must not be blank")
        String firstName,
        @NotBlank(message = "Last name must not be blank")
        String lastName,
        @NotNull(message = "Specializations must not be null")
        @Size(min = 1, message = "At least one specialization must be provided")
        List<TrainingType.Type> specializations) {

}
