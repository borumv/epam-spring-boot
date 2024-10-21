package com.vlasevsky.gym.bdd.steps;

import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.exceptions.TraineeNotFoundException;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TraineeRepository;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import com.vlasevsky.gym.service.map.TrainingServiceMap;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TrainingServiceMapSteps {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceMap trainingServiceMap;

    private TrainingCreateDto trainingCreateDto;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private Training training;

    public TrainingServiceMapSteps() {
        MockitoAnnotations.openMocks(this);
    }

    private void setupTrainingCreateRequest(String traineeUsername, String trainerUsername, String type, boolean isTypeValid) {
        trainee = new Trainee();
        trainee.setUsername(traineeUsername);

        trainer = new Trainer();
        trainer.setUsername(trainerUsername);

        if (isTypeValid) {
            trainingType = new TrainingType();
            trainingType.setName(TrainingType.Type.valueOf(type.toUpperCase()));
        } else {
            trainingType = null; // Симуляция отсутствующего типа тренировки
        }

        trainingCreateDto = new TrainingCreateDto(
                traineeUsername,
                trainerUsername,
                "Yoga Training",
                trainingType != null ? trainingType.getName() : null,
                60
        );

        training = new Training();
        training.setDate(new Date());

        Mockito.when(trainingMapper.toEntity(trainingCreateDto)).thenReturn(training);
    }

    @Given("a training create request with trainee {string}, trainer {string}, and training type {string}")
    public void aTrainingCreateRequest(String traineeUsername, String trainerUsername, String type) {
        setupTrainingCreateRequest(traineeUsername, trainerUsername, type, true);
    }

    @Given("a non-existent trainer with username {string}")
    public void aNonExistentTrainer(String username) {
        Mockito.when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
        setupTrainingCreateRequest("john_doe", username, "YOGA", true);
    }

    @When("the training is created")
    public void createTraining() {
        Mockito.when(traineeRepository.findByUsername(trainingCreateDto.traineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUsername(trainingCreateDto.trainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainingTypeRepository.findByName(trainingCreateDto.type())).thenReturn(Optional.of(trainingType));

        trainingServiceMap.create(trainingCreateDto);
    }

    @Then("the training should be saved")
    public void verifyTrainingSaved() {
        verify(trainingRepository).save(training);
    }
}
