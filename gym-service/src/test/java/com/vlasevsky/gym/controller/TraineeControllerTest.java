package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.service.AuthenticationService;
import com.vlasevsky.gym.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TraineeController traineeController;

    @Test
    void testRegisterTrainee() {
        TraineeRegistrationDto request = new TraineeRegistrationDto("John", "Doe", null, "123 Street");
        RegistrationResponse response = new RegistrationResponse("token", "password");

        when(authenticationService.registerTrainee(any(TraineeRegistrationDto.class))).thenReturn(response);

        ResponseEntity<RegistrationResponse> result = traineeController.registerTrainee(request);

        assertEquals(ResponseEntity.ok(response), result);
    }

    @Test
    void testHello() {
        String result = traineeController.hello();
        assertEquals("Hello, World!", result);
    }

    @Test
    void testGetTraineeProfile() {
        TraineeProfileReadDto profile = new TraineeProfileReadDto("John", "Doe", null, "123 Street", true, Collections.emptyList());

        when(traineeService.findTraineeByUsername(anyString())).thenReturn(profile);

        ResponseEntity<TraineeProfileReadDto> result = traineeController.getTraineeProfile("john.doe");

        assertEquals(ResponseEntity.ok(profile), result);
    }

    @Test
    void testUpdateTraineeProfile() {
        TraineeCreateAndUpdateDto dto = new TraineeCreateAndUpdateDto("john.doe", "John", "Doe", "123 Street", null, true);
        TraineeProfileReadDto profile = new TraineeProfileReadDto("John", "Doe", null, "123 Street", true, Collections.emptyList());

        when(traineeService.update(anyString(), any(TraineeCreateAndUpdateDto.class))).thenReturn(profile);

        ResponseEntity<TraineeProfileReadDto> result = traineeController.updateTraineeProfile("john.doe", dto);

        assertEquals(ResponseEntity.ok(profile), result);
    }

    @Test
    void testDeleteTraineeProfile() {
        ResponseEntity<Void> result = traineeController.deleteTraineeProfile("john.doe");

        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    void testChangeTraineeActiveStatus() {
        StatusUpdateDto statusDto = new StatusUpdateDto(true);

        ResponseEntity<Void> result = traineeController.changeTraineeActiveStatus("john.doe", statusDto);

        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    void testUpdateTraineeTrainersList() {
        TraineeProfileReadDto profile = new TraineeProfileReadDto("John", "Doe", null, "123 Street", true, Collections.emptyList());

        when(traineeService.updateTraineeTrainers(anyString(), anyList())).thenReturn(profile);

        ResponseEntity<TraineeProfileReadDto> result = traineeController.updateTraineeTrainersList("john.doe", Collections.singletonList("trainer1"));

        assertEquals(ResponseEntity.ok(profile), result);
    }

}
