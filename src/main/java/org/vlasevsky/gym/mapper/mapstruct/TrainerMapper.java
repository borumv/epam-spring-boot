package org.vlasevsky.gym.mapper.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vlasevsky.gym.dto.TrainerCreateDto;
import org.vlasevsky.gym.dto.TrainerReadDto;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(target = "name", expression = "java(trainer.getFirstName() + ' ' + trainer.getLastName())")
    TrainerReadDto toDto(Trainer trainer);

    Trainer toEntity(TrainerCreateDto dto);

    List<TrainerReadDto> toDTOList(List<Trainer> trainers);
}
