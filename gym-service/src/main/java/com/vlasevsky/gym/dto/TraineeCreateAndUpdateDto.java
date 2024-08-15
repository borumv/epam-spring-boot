package com.vlasevsky.gym.dto;


import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TraineeCreateAndUpdateDto(
        @NotNull
        String username,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String address,
        Date dateOfBirth,
        Boolean isActive) {
}
