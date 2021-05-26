package com.pucminas.icoleta.service.mapper;


import com.pucminas.icoleta.domain.*;
import com.pucminas.icoleta.service.dto.CollectPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectPoint} and its DTO {@link CollectPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {MaterialMapper.class, UserMapper.class})
public interface CollectPointMapper extends EntityMapper<CollectPointDTO, CollectPoint> {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    CollectPointDTO toDto(CollectPoint collectPoint);

    @Mapping(target = "removeMaterials", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(source = "createdById", target = "createdBy")
    CollectPoint toEntity(CollectPointDTO collectPointDTO);

    default CollectPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        CollectPoint collectPoint = new CollectPoint();
        collectPoint.setId(id);
        return collectPoint;
    }
}
