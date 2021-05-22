package com.pucminas.icoleta.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.pucminas.icoleta.domain.enumeration.MaterialType;

/**
 * A DTO for the {@link com.pucminas.icoleta.domain.Material} entity.
 */
public class MaterialDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private MaterialType materialType;

    
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

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaterialDTO materialDTO = (MaterialDTO) o;
        if (materialDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", materialType='" + getMaterialType() + "'" +
            "}";
    }
}
