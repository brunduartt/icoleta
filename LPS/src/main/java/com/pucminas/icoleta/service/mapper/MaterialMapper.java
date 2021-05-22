package com.pucminas.icoleta.service.mapper;


import com.pucminas.icoleta.domain.*;
import com.pucminas.icoleta.service.dto.MaterialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {


    @Mapping(target = "collectPoints", ignore = true)
    @Mapping(target = "removeCollectPoints", ignore = true)
    Material toEntity(MaterialDTO materialDTO);

    default Material fromId(Long id) {
        if (id == null) {
            return null;
        }
        Material material = new Material();
        material.setId(id);
        return material;
    }
}
