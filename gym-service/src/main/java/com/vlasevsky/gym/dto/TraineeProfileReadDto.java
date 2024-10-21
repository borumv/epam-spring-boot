package com.vlasevsky.gym.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record TraineeProfileReadDto(String firstName,
                                    String lastName,
                                    Date dateOfBirth,
                                    String address,
                                    Boolean isActive,
                                    List<TrainerReadDto> trainers) {
}
