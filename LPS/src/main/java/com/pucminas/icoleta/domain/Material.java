package com.pucminas.icoleta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.pucminas.icoleta.domain.enumeration.MaterialType;

/**
 * A Material.
 */
@Entity
@Table(name = "material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private MaterialType materialType;

    @ManyToMany(mappedBy = "materials")
    @JsonIgnore
    private Set<CollectPoint> collectPoints = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Material name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public Material materialType(MaterialType materialType) {
        this.materialType = materialType;
        return this;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public Set<CollectPoint> getCollectPoints() {
        return collectPoints;
    }

    public Material collectPoints(Set<CollectPoint> collectPoints) {
        this.collectPoints = collectPoints;
        return this;
    }

    public Material addCollectPoints(CollectPoint collectPoint) {
        this.collectPoints.add(collectPoint);
        collectPoint.getMaterials().add(this);
        return this;
    }

    public Material removeCollectPoints(CollectPoint collectPoint) {
        this.collectPoints.remove(collectPoint);
        collectPoint.getMaterials().remove(this);
        return this;
    }

    public void setCollectPoints(Set<CollectPoint> collectPoints) {
        this.collectPoints = collectPoints;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Material)) {
            return false;
        }
        return id != null && id.equals(((Material) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Material{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", materialType='" + getMaterialType() + "'" +
            "}";
    }
}
