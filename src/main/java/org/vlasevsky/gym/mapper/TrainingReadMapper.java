package org.vlasevsky.gym.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.dto.TrainingReadDto;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TrainingReadMapper implements Mapper<Training, TrainingReadDto> {

    private final TraineeReadMapper traineeReadMapper;
    private final TrainerReadMapper trainerReadMapper;

    @Override
    public TrainingReadDto mapFrom(Training object) {
        return new TrainingReadDto(
                object.getId(),
                object.getName(),
                object.getDate(),
                object.getDuration(),
                Optional.ofNullable(object.getTrainee())
                        .map(traineeReadMapper::mapFrom)
                        .orElse(null),
                Optional.ofNullable(object.getTrainer())
                        .map(trainerReadMapper::mapFrom)
                        .orElse(null)
        );

    }
}
