package br.com.rockstars.config;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class MinioHealthCheck implements HealthCheck {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket.album-covers")
    String albumCoversBucket;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("MinIO connection");

        try {
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(albumCoversBucket).build()
            );
            builder.up()
                .withData("bucket", albumCoversBucket)
                .withData("bucketExists", bucketExists);
        } catch (Exception e) {
            builder.down()
                .withData("error", e.getMessage());
        }

        return builder.build();
    }
}
