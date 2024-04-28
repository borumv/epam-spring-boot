package org.vlasevsky.gym.mapper.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vlasevsky.gym.dto.TrainingCreateDto;
import org.vlasevsky.gym.dto.TrainingReadDto;
import org.vlasevsky.gym.model.Training;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class, TraineeMapper.class} )
public interface TrainingMapper {
    TrainingReadDto toDto(Training training);
    List<TrainingReadDto> toDTOList(List<Training> trainings);

    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "trainingType", ignore = true)
    Training toEntity(TrainingCreateDto createDto);
}
