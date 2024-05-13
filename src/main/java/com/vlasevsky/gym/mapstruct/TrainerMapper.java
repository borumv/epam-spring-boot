package com.vlasevsky.gym.mapstruct;

import com.vlasevsky.gym.dto.TrainerCreateDto;
import com.vlasevsky.gym.dto.TrainerProfileReadDto;
import com.vlasevsky.gym.dto.TrainerReadDto;
import com.vlasevsky.gym.dto.TrainerRegistrationDto;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TrainerMapper {


    @Mapping(target = "specialization", source = "specializations", qualifiedByName = "trainingTypeToType")
    TrainerReadDto toDto(Trainer trainer);

    @Mapping(target = "specialization", source = "specializations", qualifiedByName = "trainingTypeToType")
    TrainerProfileReadDto toProfileDto(Trainer trainer);


    @Named("trainingTypeToType")
    static List<TrainingType.Type> mapSpecializations(List<TrainingType> specializations) {
        if (specializations == null) return null;
        return specializations.stream()
                .map(TrainingType::getName)
                .collect(Collectors.toList());
    }

    Trainer toEntity(TrainerCreateDto dto);
    Trainer toEntity(TrainerRegistrationDto dto);

    List<TrainerReadDto> toDTOList(List<Trainer> trainers);
    List<TrainerProfileReadDto> toDTOProfileList(List<Training> trainings);
}
