package org.vlasevsky.gym.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.vlasevsky.gym.model.*;

@Configuration
@PropertySource("classpath:hibernate.properties")
@ComponentScan(basePackages = "org.vlasevsky")
public class ApplicationConfiguration {


    @Bean
    public SessionFactory sessionFactory() {

        return new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .build())
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Trainer.class)
                .addAnnotatedClass(TrainingType.class)
                .addAnnotatedClass(Trainee.class)
                .addAnnotatedClass(Training.class)
                .buildMetadata()
                .buildSessionFactory();
    }
}
