package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.AuthenticationRequest;
import com.vlasevsky.gym.dto.AuthenticationResponse;
import com.vlasevsky.gym.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest("username", "password");
        AuthenticationResponse response = new AuthenticationResponse("token");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.authenticate(request);

        assertEquals(ResponseEntity.ok(response), result);
    }
}
