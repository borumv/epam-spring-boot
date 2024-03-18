package org.vlasevsky.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.storage.Storage;
import org.vlasevsky.gym.storage.StorageImpl;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "org.vlasevsky")
public class ApplicationConfiguration {

    @Bean
    public Storage<Trainee> traineeStorage() {
        return new StorageImpl<>(Trainee.class);
    }

    @Bean
    public Storage<Trainer> trainerStorage() {
        return new StorageImpl<>(Trainer.class);
    }

    @Bean
    public Storage<Training> trainingStorage() {
        return new StorageImpl<>(Training.class);
    }

}
