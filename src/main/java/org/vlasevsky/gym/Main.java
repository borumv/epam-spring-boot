package org.vlasevsky.gym;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.vlasevsky.gym.config.ApplicationConfiguration;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.storage.Storage;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println("Bean Name: "+ bean);
        }
        ctx.getApplicationName();

        Storage<Trainee> traineeStorage = ctx.getBean("traineeStorage", Storage.class);
        Storage<Trainer> trainerStorage = ctx.getBean("trainerStorage", Storage.class);
        Storage<Training> trainingStorage = ctx.getBean("trainingStorage", Storage.class);

        System.out.println("Trainees:");
        for (Trainee trainee : traineeStorage.findAll()) {
            System.out.println(trainee);
        }

        System.out.println("\nTrainers:");
        for (Trainer trainer : trainerStorage.findAll()) {
            System.out.println(trainer);
        }

        System.out.println("\nTrainings:");
        for (Training training : trainingStorage.findAll()) {
            System.out.println(training);
        }

    }
}