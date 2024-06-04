package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.AuthenticationRequest;
import com.vlasevsky.gym.dto.AuthenticationResponse;
import com.vlasevsky.gym.dto.TraineeRegistrationDto;
import com.vlasevsky.gym.dto.TrainerRegistrationDto;
import com.vlasevsky.gym.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}