package com.vlasevsky.gym.bdd.steps;

import com.vlasevsky.gym.dto.StatusUpdateDto;
import com.vlasevsky.gym.dto.TrainerCreateDto;
import com.vlasevsky.gym.dto.TrainerProfileReadDto;
import com.vlasevsky.gym.dto.TrainerReadDto;
import com.vlasevsky.gym.mapstruct.TrainerMapper;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import com.vlasevsky.gym.service.map.TrainerServiceMap;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TrainerServiceMapSteps {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainingMapper trainingMapper;
    @InjectMocks
    private TrainerServiceMap trainerServiceMap;
    private Trainer trainer;
    private TrainerProfileReadDto trainerProfile;
    private TrainerCreateDto trainerCreateDto;
    private Set<Trainer> trainers;
    private Set<Training> trainings;

    public TrainerServiceMapSteps() {
        MockitoAnnotations.openMocks(this);
        initTestData();
    }

    public void initTestData() {
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("trainer1");
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        trainerProfile = new TrainerProfileReadDto(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        trainerCreateDto = new TrainerCreateDto(
                trainer.getFirstName(),
                trainer.getLastName(),
                List.of(TrainingType.Type.YOGA, TrainingType.Type.PILATES),
                true
        );

        trainers = new HashSet<>(Collections.singleton(trainer));
        trainings = new HashSet<>();
    }

    @Given("a trainer exists with username {string}")
    public void aTrainerExistsWithUsername(String username) {
        trainer.setUsername(username);

        Mockito.when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        Mockito.when(trainerMapper.toProfileDto(trainer)).thenReturn(trainerProfile);
    }

    @When("I search for trainer with username {string}")
    public void findTrainerByUsername(String username) {
        trainerServiceMap.findTrainerByUsername(username);
    }

    @Then("the trainer profile should be returned")
    public void verifyTrainerProfile() {
        verify(trainerRepository).findByUsername(trainer.getUsername());
        assertEquals(trainer.getUsername(), trainerProfile.username());
    }

    @When("I update trainer {string} with first name {string}, last name {string}, and specializations {string}, {string}")
    public void updateTrainer(String username, String firstName, String lastName, String specialization1, String specialization2) {
        List<TrainingType.Type> specializationsEnum = Arrays.asList(
                TrainingType.Type.valueOf(specialization1.toUpperCase()),
                TrainingType.Type.valueOf(specialization2.toUpperCase())
        );

        Set<TrainingType> trainingTypes = new HashSet<>();
        specializationsEnum.forEach(type -> {
            TrainingType trainingType = new TrainingType();
            trainingType.setName(type);
            trainingTypes.add(trainingType);
        });
        Mockito.when(trainingTypeRepository.findByNames(specializationsEnum)).thenReturn(trainingTypes);

        trainerCreateDto = new TrainerCreateDto(firstName, lastName, specializationsEnum, true);
        trainerServiceMap.update(username, trainerCreateDto);
    }

    @Then("the details of trainer {string} should be updated")
    public void verifyUpdatedTrainer(String username) {
        verify(trainerRepository).save(trainer);
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
    }

    @When("I fetch all trainers")
    public void fetchAllTrainers() {
        Mockito.when(trainerRepository.findAll()).thenReturn(new ArrayList<>(trainers));

        Set<TrainerReadDto> trainerDtos = new HashSet<>(Collections.singletonList(
                new TrainerReadDto(trainer.getId(), trainer.getUsername(), trainer.getFirstName(), trainer.getLastName(), List.of())
        ));
        Mockito.when(trainerMapper.toDTOList(Mockito.anySet())).thenReturn(trainerDtos);

        trainerServiceMap.findAll();
    }

    @Then("a list of trainers should be returned")
    public void verifyAllTrainers() {
        verify(trainerRepository).findAll();
        assertEquals(1, trainers.size());
    }

    @When("I change the active status of trainer {string} to {string}")
    public void changeActiveStatus(String username, String isActive) {
        Mockito.when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        trainerServiceMap.changeActiveStatus(username, new StatusUpdateDto(Boolean.parseBoolean(isActive)));
    }

    @Then("the active status of trainer {string} should be {string}")
    public void verifyTrainerStatus(String username, String expectedStatus) {
        verify(trainerRepository).save(trainer);
        assertEquals(Boolean.parseBoolean(expectedStatus), trainer.getIsActive());
    }

    @When("I fetch the trainings of trainer {string} from {string} to {string}")
    public void fetchTrainings(String trainerUsername, String from, String to) {
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime toDate = LocalDateTime.parse(to);
        Date trainingDate = Date.from(fromDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant());

        Training training = new Training();
        training.setId(1L);
        training.setTrainer(trainer);
        training.setDate(trainingDate);
        trainings.add(training);

        Mockito.when(trainingRepository.findTrainingsByTrainerAndPeriodAndTrainee(trainerUsername, fromDate, toDate, null))
                .thenReturn(trainings);
        Mockito.when(trainingMapper.toDTOList(trainings)).thenReturn(new HashSet<>());

        trainerServiceMap.getTrainerTrainings(trainerUsername, fromDate, toDate, null);
    }

    @Then("a list of trainings should be returned")
    public void verifyTrainings() {
        verify(trainingRepository).findTrainingsByTrainerAndPeriodAndTrainee(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any());
    }
}
