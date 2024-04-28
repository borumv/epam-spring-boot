package org.vlasevsky.gym.dto;

import org.vlasevsky.gym.model.TrainingType;

import java.util.List;

public record TrainerReadDto(Long id,
                             String username,
                             String firstName,
                             String lastName,
                             List<TrainingType.Type> specializations
) {

}
