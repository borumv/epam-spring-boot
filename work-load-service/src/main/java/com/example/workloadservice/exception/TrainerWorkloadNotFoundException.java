package com.example.workloadservice.exception;

public class TrainerWorkloadNotFoundException extends RuntimeException {
    public TrainerWorkloadNotFoundException(String username) {
        super("Workload with trainer " + username + " is empty");
    }
}
