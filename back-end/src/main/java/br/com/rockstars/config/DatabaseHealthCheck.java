package br.com.rockstars.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import java.sql.Connection;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("Database connection");

        try (Connection connection = dataSource.getConnection()) {
            boolean valid = connection.isValid(5);
            if (valid) {
                builder.up()
                    .withData("database", connection.getMetaData().getDatabaseProductName())
                    .withData("version", connection.getMetaData().getDatabaseProductVersion());
            } else {
                builder.down()
                    .withData("error", "Connection validation failed");
            }
        } catch (Exception e) {
            builder.down()
                .withData("error", e.getMessage());
        }

        return builder.build();
    }
}
