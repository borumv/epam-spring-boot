package org.vlasevsky.gym.mapper.mapstruct;

import org.mapstruct.Mapper;
import org.vlasevsky.gym.dto.UserReadDto;
import org.vlasevsky.gym.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserReadDto toDto(User user);
}
