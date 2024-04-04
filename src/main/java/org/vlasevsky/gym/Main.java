package org.vlasevsky.gym;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.vlasevsky.gym.config.ApplicationConfiguration;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import org.vlasevsky.gym.dto.TrainerCreateDto;
import org.vlasevsky.gym.service.map.TraineeServiceMap;
import org.vlasevsky.gym.service.map.TrainerServiceMap;

import java.util.Date;

@Slf4j
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            log.info("Bean Name: " + bean);
        }

    }
}
