package com.vlasevsky.gym.mapstruct;

import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.dto.TrainingReadDto;
import com.vlasevsky.gym.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
