package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.Album;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlbumRequestDTO {

    @NotBlank(message = "Titulo do album e obrigatorio")
    @Size(max = 255, message = "Titulo deve ter no maximo 255 caracteres")
    private String title;

    private Integer releaseYear;

    private Boolean active;

    public AlbumRequestDTO() {
    }

    public AlbumRequestDTO(String title, Integer releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public Album toEntity() {
        Album album = new Album();
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
}
