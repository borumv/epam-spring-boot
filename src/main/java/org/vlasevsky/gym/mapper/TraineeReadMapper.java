package org.vlasevsky.gym.mapper;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.dto.TraineeReadDto;

@Component
public class TraineeReadMapper implements Mapper<Trainee, TraineeReadDto> {
    @Override
    public TraineeReadDto mapFrom(Trainee object) {
        return new TraineeReadDto(
                object.getId(),
                object.getFirstName()
        );
    }
}
