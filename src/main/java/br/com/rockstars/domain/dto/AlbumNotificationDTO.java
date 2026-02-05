package br.com.rockstars.domain.dto;

import java.time.LocalDateTime;

public class AlbumNotificationDTO {

    private String type;
    private Long albumId;
    private String albumTitle;
    private Integer releaseYear;
    private LocalDateTime timestamp;

    public AlbumNotificationDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public AlbumNotificationDTO(String type, Long albumId, String albumTitle, Integer releaseYear) {
        this.type = type;
        this.albumId = albumId;
        this.albumTitle = albumTitle;
        this.releaseYear = releaseYear;
        this.timestamp = LocalDateTime.now();
    }

    public static AlbumNotificationDTO created(AlbumDTO album) {
        return new AlbumNotificationDTO("ALBUM_CREATED", album.getId(), album.getTitle(), album.getReleaseYear());
    }

    public static AlbumNotificationDTO updated(AlbumDTO album) {
        return new AlbumNotificationDTO("ALBUM_UPDATED", album.getId(), album.getTitle(), album.getReleaseYear());
    }

    public static AlbumNotificationDTO deleted(Long albumId) {
        return new AlbumNotificationDTO("ALBUM_DELETED", albumId, null, null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
