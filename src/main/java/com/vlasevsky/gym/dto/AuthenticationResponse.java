package com.vlasevsky.gym.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
