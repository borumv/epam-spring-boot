package com.vlasevsky.gym.mapstruct;


import com.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import com.vlasevsky.gym.dto.TraineeProfileReadDto;
import com.vlasevsky.gym.dto.TraineeReadDto;
import com.vlasevsky.gym.dto.TraineeRegistrationDto;
import com.vlasevsky.gym.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(target = "username", expression = "java(trainee.getUsername())")
    TraineeReadDto toDto(Trainee trainee);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "isActive", source = "isActive")
    Trainee toEntity(TraineeCreateAndUpdateDto dto);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    Trainee toEntity(TraineeRegistrationDto dto);

    TraineeCreateAndUpdateDto toCreateAndUpdateDto(TraineeRegistrationDto registrationDto);

    @Mapping(target = "trainers", expression = "java(new java.util.ArrayList<>())")
    TraineeProfileReadDto toTraineeProfileDto(Trainee trainee);

}
