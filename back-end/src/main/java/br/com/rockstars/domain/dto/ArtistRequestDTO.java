package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.Artist;
import br.com.rockstars.domain.enums.ArtistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ArtistRequestDTO {

    @NotBlank(message = "Nome do artista e obrigatorio")
    @Size(max = 255, message = "Nome deve ter no maximo 255 caracteres")
    private String name;

    @NotNull(message = "Tipo do artista e obrigatorio")
    private ArtistType type;

    private Boolean active;

    private Long regionalId;

    public ArtistRequestDTO() {
    }

    public ArtistRequestDTO(String name, ArtistType type) {
        this.name = name;
        this.type = type;
    }

    public Artist toEntity() {
        Artist artist = new Artist();
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

    public Long getRegionalId() {
        return regionalId;
    }

    public void setRegionalId(Long regionalId) {
        this.regionalId = regionalId;
    }
}
