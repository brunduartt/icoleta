package com.pucminas.icoleta.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A CollectPoint.
 */
@Entity
@Table(name = "collect_point")
public class CollectPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    @NotNull
    @Column(name = "lon", nullable = false)
    private Double lon;

    @ManyToMany
    @JoinTable(name = "collect_point_materials",
               joinColumns = @JoinColumn(name = "collect_point_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "materials_id", referencedColumnName = "id"))
    private Set<Material> materials = new HashSet<>();

    @ManyToMany(mappedBy = "collectPoints")
    private Set<User> users = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("collectPoints")
    private User createdBy;

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

    public CollectPoint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CollectPoint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public CollectPoint lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public CollectPoint lon(Double lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public CollectPoint materials(Set<Material> materials) {
        this.materials = materials;
        return this;
    }

    public CollectPoint addMaterials(Material material) {
        this.materials.add(material);
        material.getCollectPoints().add(this);
        return this;
    }

    public CollectPoint removeMaterials(Material material) {
        this.materials.remove(material);
        material.getCollectPoints().remove(this);
        return this;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public Set<User> getUsers() {
        return users;
    }

    public CollectPoint users(Set<User> users) {
        this.users = users;
        return this;
    }

    public CollectPoint addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public CollectPoint removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectPoint)) {
            return false;
        }
        return id != null && id.equals(((CollectPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CollectPoint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            "}";
    }
}
