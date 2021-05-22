package com.pucminas.icoleta.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.pucminas.icoleta.domain.enumeration.MaterialType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pucminas.icoleta.domain.Material} entity. This class is used
 * in {@link com.pucminas.icoleta.web.rest.MaterialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /materials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MaterialCriteria implements Serializable, Criteria {
    /**
     * Class for filtering MaterialType
     */
    public static class MaterialTypeFilter extends Filter<MaterialType> {

        public MaterialTypeFilter() {
        }

        public MaterialTypeFilter(MaterialTypeFilter filter) {
            super(filter);
        }

        @Override
        public MaterialTypeFilter copy() {
            return new MaterialTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private MaterialTypeFilter materialType;

    private LongFilter collectPointsId;

    public MaterialCriteria() {
    }

    public MaterialCriteria(MaterialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.materialType = other.materialType == null ? null : other.materialType.copy();
        this.collectPointsId = other.collectPointsId == null ? null : other.collectPointsId.copy();
    }

    @Override
    public MaterialCriteria copy() {
        return new MaterialCriteria(this);
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

    public MaterialTypeFilter getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialTypeFilter materialType) {
        this.materialType = materialType;
    }

    public LongFilter getCollectPointsId() {
        return collectPointsId;
    }

    public void setCollectPointsId(LongFilter collectPointsId) {
        this.collectPointsId = collectPointsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MaterialCriteria that = (MaterialCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(materialType, that.materialType) &&
            Objects.equals(collectPointsId, that.collectPointsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        materialType,
        collectPointsId
        );
    }

    @Override
    public String toString() {
        return "MaterialCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (materialType != null ? "materialType=" + materialType + ", " : "") +
                (collectPointsId != null ? "collectPointsId=" + collectPointsId + ", " : "") +
            "}";
    }

}
