package com.pucminas.icoleta.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.pucminas.icoleta.domain.CollectPoint} entity.
 */
public class CollectPointDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    private Set<MaterialDTO> materials = new HashSet<>();

    private Long createdById;

    private String createdByLogin;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Set<MaterialDTO> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<MaterialDTO> materials) {
        this.materials = materials;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String createdByLogin) {
        this.createdByLogin = createdByLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollectPointDTO collectPointDTO = (CollectPointDTO) o;
        if (collectPointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collectPointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CollectPointDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", materials='" + getMaterials() + "'" +
            "}";
    }
}
