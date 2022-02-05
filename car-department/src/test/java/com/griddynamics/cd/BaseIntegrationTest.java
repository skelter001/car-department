package com.griddynamics.cd;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
public class BaseIntegrationTest {

    protected static final PostgreSQLContainer<?> container;
    protected static final Connection connection;

    static {
        container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
                .withDatabaseName("car_department_database")
                .withUsername("admin")
                .withPassword("password")
                .withReuse(true);

        container.start();

        try {
            connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("Connection error");
        }
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
}
