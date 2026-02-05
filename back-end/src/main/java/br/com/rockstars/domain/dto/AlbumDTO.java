package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.Album;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumDTO {

    private Long id;

    @NotBlank(message = "Titulo do album e obrigatorio")
    @Size(max = 255, message = "Titulo deve ter no maximo 255 caracteres")
    private String title;

    private Integer releaseYear;

    private Boolean active;

    private List<ArtistDTO> artists;

    private List<AlbumCoverDTO> covers;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AlbumDTO() {
    }

    public AlbumDTO(Long id, String title, Integer releaseYear, Boolean active) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.active = active;
    }

    public static AlbumDTO fromEntity(Album album) {
        if (album == null) {
            return null;
        }
        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setReleaseYear(album.getReleaseYear());
        dto.setActive(album.getActive());
        dto.setCreatedAt(album.getCreatedAt());
        dto.setUpdatedAt(album.getUpdatedAt());

        if (album.getArtists() != null) {
            dto.setArtists(album.getArtists().stream()
                .map(ArtistDTO::fromEntity)
                .collect(Collectors.toList()));
        }

        if (album.getCovers() != null) {
            dto.setCovers(album.getCovers().stream()
                .map(AlbumCoverDTO::fromEntity)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    public static AlbumDTO fromEntityWithoutRelations(Album album) {
        if (album == null) {
            return null;
        }
        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setReleaseYear(album.getReleaseYear());
        dto.setActive(album.getActive());
        dto.setCreatedAt(album.getCreatedAt());
        dto.setUpdatedAt(album.getUpdatedAt());
        return dto;
    }

    public Album toEntity() {
        Album album = new Album();
        album.setId(this.id);
        album.setTitle(this.title);
        album.setReleaseYear(this.releaseYear);
        album.setActive(this.active != null ? this.active : true);
        return album;
    }

    public void updateEntity(Album album) {
        if (this.title != null) {
            album.setTitle(this.title);
        }
        if (this.releaseYear != null) {
            album.setReleaseYear(this.releaseYear);
        }
        if (this.active != null) {
            album.setActive(this.active);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public List<AlbumCoverDTO> getCovers() {
        return covers;
    }

    public void setCovers(List<AlbumCoverDTO> covers) {
        this.covers = covers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
