package org.vlasevsky.gym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.vlasevsky.gym.config.ApplicationConfiguration;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import org.vlasevsky.gym.dto.TrainerCreateDto;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.service.map.TraineeServiceMap;
import org.vlasevsky.gym.service.map.TrainerServiceMap;
import org.vlasevsky.gym.service.map.TrainingServiceMap;

import java.util.Date;


public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println("Bean Name: " + bean);
        }
        ctx.getApplicationName();

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        TraineeServiceMap traineeService = context.getBean(TraineeServiceMap.class);
        TrainerServiceMap trainerService = context.getBean(TrainerServiceMap.class);

        TraineeCreateAndUpdateDto newTraineeDto = new TraineeCreateAndUpdateDto("Boris ", "Vlas", "123 Main St", new Date(), true);
        CredentialsDto traineeCredentials = traineeService.create(newTraineeDto);
        System.out.println("Trainee created with credentials: " + traineeCredentials);
        TrainerCreateDto newTrainerDto = new TrainerCreateDto("Borist", "Vlast", true);
        CredentialsDto trainerCredentials = trainerService.create(newTrainerDto);
        System.out.println("Trainer created with credentials: " + trainerCredentials);
        System.out.println("Trainee details: " + traineeService.findTraineeByUsername(traineeCredentials.getUsername(), traineeCredentials));
        System.out.println("Trainer details: " + trainerService.findTrainerByUsername(trainerCredentials.getUsername(), trainerCredentials));
        traineeService.activateTrainee(traineeService.findTraineeByUsername(traineeCredentials.getUsername(), traineeCredentials).id(), traineeCredentials);
        trainerService.activateTrainer(trainerService.findTrainerByUsername(trainerCredentials.getUsername(), trainerCredentials).id(), trainerCredentials);
        traineeService.deactivateTrainee(traineeService.findTraineeByUsername(traineeCredentials.getUsername(), traineeCredentials).id(), traineeCredentials);
        trainerService.deactivateTrainer(trainerService.findTrainerByUsername(trainerCredentials.getUsername(), trainerCredentials).id(), trainerCredentials);
    }
}