package org.vlasevsky.gym.dto;


import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
