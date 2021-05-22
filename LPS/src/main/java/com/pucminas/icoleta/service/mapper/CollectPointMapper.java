package com.pucminas.icoleta.service.mapper;


import com.pucminas.icoleta.domain.*;
import com.pucminas.icoleta.service.dto.CollectPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectPoint} and its DTO {@link CollectPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {MaterialMapper.class, UserMapper.class})
public interface CollectPointMapper extends EntityMapper<CollectPointDTO, CollectPoint> {


    @Mapping(target = "removeMaterials", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)

    default CollectPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        CollectPoint collectPoint = new CollectPoint();
        collectPoint.setId(id);
        return collectPoint;
    }
}
