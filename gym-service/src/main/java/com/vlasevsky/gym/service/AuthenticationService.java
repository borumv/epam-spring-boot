package com.vlasevsky.gym.service;

import com.vlasevsky.gym.dto.*;

public interface AuthenticationService {
    RegistrationResponse registerTrainee(TraineeRegistrationDto request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    RegistrationResponse registerTrainer(TrainerRegistrationDto request);

    void logout(String token);
}
