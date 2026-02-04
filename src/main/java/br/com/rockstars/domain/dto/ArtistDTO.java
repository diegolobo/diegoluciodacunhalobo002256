package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.enums.ArtistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ArtistDTO {

    private Long id;

    @NotBlank(message = "Nome do artista e obrigatorio")
    @Size(max = 255, message = "Nome deve ter no maximo 255 caracteres")
    private String name;

    @NotNull(message = "Tipo do artista e obrigatorio")
    private ArtistType type;

    private Boolean active;

    private Integer albumCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ArtistDTO() {
    }

    public ArtistDTO(Long id, String name, ArtistType type, Boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.active = active;
    }

    public static ArtistDTO fromEntity(Artist artist) {
        if (artist == null) {
            return null;
        }
        ArtistDTO dto = new ArtistDTO();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setType(artist.getType());
        dto.setActive(artist.getActive());
        dto.setAlbumCount(artist.getAlbums() != null ? artist.getAlbums().size() : 0);
        dto.setCreatedAt(artist.getCreatedAt());
        dto.setUpdatedAt(artist.getUpdatedAt());
        return dto;
    }

    public Artist toEntity() {
        Artist artist = new Artist();
        artist.setId(this.id);
        artist.setName(this.name);
        artist.setType(this.type);
        artist.setActive(this.active != null ? this.active : true);
        return artist;
    }

    public void updateEntity(Artist artist) {
        if (this.name != null) {
            artist.setName(this.name);
        }
        if (this.type != null) {
            artist.setType(this.type);
        }
        if (this.active != null) {
            artist.setActive(this.active);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArtistType getType() {
        return type;
    }

    public void setType(ArtistType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
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
