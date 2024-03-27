package org.vlasevsky.gym.mapper;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import org.vlasevsky.gym.model.Trainee;
@Component
public class TraineeCreateMapper implements Mapper<TraineeCreateAndUpdateDto, Trainee> {
    @Override
    public Trainee mapFrom(TraineeCreateAndUpdateDto dto) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(dto.firstName());
        trainee.setLastName(dto.lastName());
        trainee.setIsActive(dto.isActive());
        trainee.setAddress(dto.address());
        return trainee;
    }
}
