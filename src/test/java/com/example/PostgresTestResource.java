package com.example;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.HashMap;
import java.util.Map;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {

    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Override
    public Map<String, String> start() {
        db.start();
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", db.getJdbcUrl());
        config.put("quarkus.datasource.username", db.getUsername());
        config.put("quarkus.datasource.password", db.getPassword());
        return config;
    }

    @Override
    public void stop() {
        db.stop();
    }
}