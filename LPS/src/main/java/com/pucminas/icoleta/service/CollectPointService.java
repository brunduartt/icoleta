package com.pucminas.icoleta.service;

import com.pucminas.icoleta.domain.CollectPoint;
import com.pucminas.icoleta.repository.CollectPointRepository;
import com.pucminas.icoleta.service.dto.CollectPointDTO;
import com.pucminas.icoleta.service.mapper.CollectPointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CollectPoint}.
 */
@Service
@Transactional
public class CollectPointService {

    private final Logger log = LoggerFactory.getLogger(CollectPointService.class);

    private final CollectPointRepository collectPointRepository;

    private final CollectPointMapper collectPointMapper;

    public CollectPointService(CollectPointRepository collectPointRepository, CollectPointMapper collectPointMapper) {
        this.collectPointRepository = collectPointRepository;
        this.collectPointMapper = collectPointMapper;
    }

    /**
     * Save a collectPoint.
     *
     * @param collectPointDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectPointDTO save(CollectPointDTO collectPointDTO) {
        log.debug("Request to save CollectPoint : {}", collectPointDTO);
        CollectPoint collectPoint = collectPointMapper.toEntity(collectPointDTO);
        collectPoint = collectPointRepository.save(collectPoint);
        return collectPointMapper.toDto(collectPoint);
    }

    /**
     * Get all the collectPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollectPoints");
        return collectPointRepository.findAll(pageable)
            .map(collectPointMapper::toDto);
    }

    /**
     * Get all the collectPoints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CollectPointDTO> findAllWithEagerRelationships(Pageable pageable) {
        return collectPointRepository.findAllWithEagerRelationships(pageable).map(collectPointMapper::toDto);
    }

    /**
     * Get one collectPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectPointDTO> findOne(Long id) {
        log.debug("Request to get CollectPoint : {}", id);
        return collectPointRepository.findOneWithEagerRelationships(id)
            .map(collectPointMapper::toDto);
    }

    /**
     * Delete the collectPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CollectPoint : {}", id);
        collectPointRepository.deleteById(id);
    }
}
