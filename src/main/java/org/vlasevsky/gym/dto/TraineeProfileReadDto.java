package org.vlasevsky.gym.dto;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileReadDto(String firstName,
                                    String lastName,
                                    LocalDate dateOfBirth,
                                    String address,
                                    Boolean isActive,
                                    List<TrainerReadDto> trainers) {
}
