package br.com.rockstars.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MinioConfig {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket.album-covers")
    String albumCoversBucket;

    void onStart(@Observes StartupEvent ev) {
        createBucketIfNotExists(albumCoversBucket);
    }

    private void createBucketIfNotExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );
                Log.info("Bucket criado: " + bucketName);
            } else {
                Log.info("Bucket ja existe: " + bucketName);
            }
        } catch (Exception e) {
            Log.error("Erro ao criar bucket: " + bucketName, e);
        }
    }
}
