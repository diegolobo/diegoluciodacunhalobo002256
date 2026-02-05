package br.com.rockstars.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "regional")
public class Regional extends BaseEntity {

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Regional() {
    }

    public Regional(Long externalId, String name) {
        this.externalId = externalId;
        this.name = name;
        this.active = true;
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
}
