package com.example.workloadservice.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationTestBase {

    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

    @BeforeAll
    static void runContainers() {

        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}