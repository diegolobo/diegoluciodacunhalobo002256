package br.com.rockstars.domain.dto;

import br.com.rockstars.domain.entity.Regional;
import java.time.LocalDateTime;

public class RegionalDTO {

    private Long id;
    private Long externalId;
    private String name;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RegionalDTO() {
    }

    public RegionalDTO(Long id, Long externalId, String name, Boolean active) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.active = active;
    }

    public static RegionalDTO fromEntity(Regional regional) {
        if (regional == null) {
            return null;
        }
        RegionalDTO dto = new RegionalDTO();
        dto.setId(regional.getId());
        dto.setExternalId(regional.getExternalId());
        dto.setName(regional.getName());
        dto.setActive(regional.getActive());
        dto.setCreatedAt(regional.getCreatedAt());
        dto.setUpdatedAt(regional.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
