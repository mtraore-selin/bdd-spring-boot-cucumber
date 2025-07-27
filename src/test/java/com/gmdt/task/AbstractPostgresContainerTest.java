package com.gmdt.task;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractPostgresContainerTest {

    private static final boolean isCi = System.getenv("GITHUB_ACTIONS") != null;

    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("task_test_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        if (isCi) {
            // GitHub Actions: DB is already provided via services
            registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/task_test_db");
        } else {
            // Local: start the container
            postgresContainer.start();
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        }

        registry.add("spring.datasource.username", () -> "testuser");
        registry.add("spring.datasource.password", () -> "testpass");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
}
