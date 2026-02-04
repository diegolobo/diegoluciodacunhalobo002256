package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.AlbumCover;
import java.time.LocalDateTime;

public class AlbumCoverDTO {

    private Long id;

    private Long albumId;

    private String fileName;

    private String minioKey;

    private String contentType;

    private Long fileSize;

    private String presignedUrl;

    private LocalDateTime createdAt;

    public AlbumCoverDTO() {
    }

    public AlbumCoverDTO(Long id, Long albumId, String fileName, String minioKey) {
        this.id = id;
        this.albumId = albumId;
        this.fileName = fileName;
        this.minioKey = minioKey;
    }

    public static AlbumCoverDTO fromEntity(AlbumCover cover) {
        if (cover == null) {
            return null;
        }
        AlbumCoverDTO dto = new AlbumCoverDTO();
        dto.setId(cover.getId());
        dto.setAlbumId(cover.getAlbum() != null ? cover.getAlbum().getId() : null);
        dto.setFileName(cover.getFileName());
        dto.setMinioKey(cover.getMinioKey());
        dto.setContentType(cover.getContentType());
        dto.setFileSize(cover.getFileSize());
        dto.setCreatedAt(cover.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMinioKey() {
        return minioKey;
    }

    public void setMinioKey(String minioKey) {
        this.minioKey = minioKey;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
