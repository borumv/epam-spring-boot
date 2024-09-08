package com.example.workloadservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadRequest {
    @NotBlank
    private String transactionId;

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean isActive;

    @NotNull
    private LocalDate trainingDate;

    @Positive
    private int trainingDuration;

    @NotNull
    private ActionType actionType;

    public enum ActionType {
        ADD,
        DELETE
    }
}
