package org.vlasevsky.gym.mapper.mapstruct;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import org.vlasevsky.gym.dto.TraineeReadDto;
import org.vlasevsky.gym.model.Trainee;
@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(target = "name", expression = "java(trainee.getFirstName() + ' ' + trainee.getLastName())")
    TraineeReadDto toDto(Trainee trainee);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "isActive", source = "isActive")
    Trainee toEntity(TraineeCreateAndUpdateDto dto);


}
