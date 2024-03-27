package org.vlasevsky.gym.dto;

public record UserReadDto(Long id,
                          String firstName,
                          String lastName,
                          String username
) {

}
