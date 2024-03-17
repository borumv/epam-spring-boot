package org.vlasevsky.gym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.vlasevsky.gym.config.ApplicationConfiguration;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.storage.Storage;
import org.vlasevsky.gym.storage.TraineeStorage;
import org.vlasevsky.gym.storage.TrainerStorage;
import org.vlasevsky.gym.storage.TrainingStorage;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println("Bean Name: "+ bean);
        }
        ctx.getApplicationName();

        Storage<Trainee> traineeStorage = ctx.getBean(TraineeStorage.class);
        Storage<Trainer> trainerStorage = ctx.getBean(TrainerStorage.class);
        Storage<Training> trainingStorage = ctx.getBean(TrainingStorage.class);

        System.out.println("Trainees:");
        traineeStorage.getAllData().values().forEach(System.out::println);

        System.out.println("\nTrainers:");
        trainerStorage.getAllData().values().forEach(System.out::println);

        System.out.println("\nTrainings:");
        trainingStorage.getAllData().values().forEach(System.out::println);

    }
}