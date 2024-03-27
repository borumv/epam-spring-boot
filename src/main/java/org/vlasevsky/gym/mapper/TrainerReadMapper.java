package org.vlasevsky.gym.mapper;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.dto.TrainerReadDto;

@Component
public class TrainerReadMapper implements Mapper<Trainer, TrainerReadDto> {
    @Override
    public TrainerReadDto mapFrom(Trainer object) {
        return new TrainerReadDto(
                object.getId(),
                object.getFirstName()
        );
    }
}
