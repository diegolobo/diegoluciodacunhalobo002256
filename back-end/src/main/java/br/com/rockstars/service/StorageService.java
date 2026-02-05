package br.com.rockstars.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class StorageService {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket.album-covers")
    String albumCoversBucket;

    @ConfigProperty(name = "minio.public.url", defaultValue = "")
    String minioPublicUrl;

    public String uploadFile(InputStream inputStream, String fileName, String contentType, long size) {
        try {
            String minioKey = generateMinioKey(fileName);

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(albumCoversBucket)
                    .object(minioKey)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build()
            );

            return minioKey;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String minioKey) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(albumCoversBucket)
                    .object(minioKey)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo: " + e.getMessage(), e);
        }
    }

    public String getPresignedUrl(String minioKey, int expirationMinutes) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(albumCoversBucket)
                    .object(minioKey)
                    .expiry(expirationMinutes, TimeUnit.MINUTES)
                    .build()
            );

            if (minioPublicUrl != null && !minioPublicUrl.isEmpty()) {
                url = url.replaceFirst("http://[^/]+", minioPublicUrl);
            }

            return url;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL: " + e.getMessage(), e);
        }
    }

    public String getPresignedUrl(String minioKey) {
        return getPresignedUrl(minioKey, 30);
    }

    private String generateMinioKey(String originalFileName) {
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        return UUID.randomUUID().toString() + extension;
    }
}
