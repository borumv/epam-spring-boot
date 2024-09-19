package com.vlasevsky.gym.integration;

import com.vlasevsky.gym.integration.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@IT
@Sql({
        "classpath:sql/schema-test.sql",
        "classpath:sql/data-test.sql"

})
public abstract class IntegrationTestBase {

    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    private static final GenericContainer<?> activeMqContainer = new GenericContainer<>("rmohr/activemq:5.15.9");
    @BeforeAll
    static void runContainer(){
        container.start();
        activeMqContainer.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

}