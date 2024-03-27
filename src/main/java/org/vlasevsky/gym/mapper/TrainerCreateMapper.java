package org.vlasevsky.gym.mapper;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.dto.TrainerCreateDto;

@Component
public class TrainerCreateMapper implements Mapper<TrainerCreateDto, Trainer> {

    @Override
    public Trainer mapFrom(TrainerCreateDto dto) {

        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setIsActive(dto.isActive());

        return trainer;
    }
}
