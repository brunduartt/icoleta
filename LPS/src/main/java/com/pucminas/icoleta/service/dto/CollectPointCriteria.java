package com.pucminas.icoleta.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pucminas.icoleta.domain.CollectPoint} entity. This class is used
 * in {@link com.pucminas.icoleta.web.rest.CollectPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collect-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollectPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private DoubleFilter lat;

    private DoubleFilter lon;

    private LongFilter materialsId;

    private LongFilter userId;

    private StringFilter userLogin;

    private MaterialCriteria.MaterialTypeFilter materialType;

    private LongFilter createdById;

    public CollectPointCriteria() {
    }

    public CollectPointCriteria(CollectPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.lon = other.lon == null ? null : other.lon.copy();
        this.materialsId = other.materialsId == null ? null : other.materialsId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userLogin = other.userLogin == null ? null : other.userLogin.copy();
        this.materialType = other.materialType == null ? null : other.materialType.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
    }

    @Override
    public CollectPointCriteria copy() {
        return new CollectPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public DoubleFilter getLon() {
        return lon;
    }

    public void setLon(DoubleFilter lon) {
        this.lon = lon;
    }

    public LongFilter getMaterialsId() {
        return materialsId;
    }

    public void setMaterialsId(LongFilter materialsId) {
        this.materialsId = materialsId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public MaterialCriteria.MaterialTypeFilter getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialCriteria.MaterialTypeFilter materialType) {
        this.materialType = materialType;
    }

    public StringFilter getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(StringFilter userLogin) {
        this.userLogin = userLogin;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CollectPointCriteria that = (CollectPointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(materialsId, that.materialsId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        lat,
        lon,
        materialsId,
            userId
        );
    }

    @Override
    public String toString() {
        return "CollectPointCriteria{" +
            "id=" + id +
            ", name=" + name +
            ", description=" + description +
            ", lat=" + lat +
            ", lon=" + lon +
            ", materialsId=" + materialsId +
            ", userId=" + userId +
            ", userLogin=" + userLogin +
            ", materialType=" + materialType +
            ", createdById=" + createdById +
            '}';
    }

}
