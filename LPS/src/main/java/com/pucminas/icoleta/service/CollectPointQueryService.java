package com.pucminas.icoleta.service;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.pucminas.icoleta.domain.CollectPoint;
import com.pucminas.icoleta.domain.*; // for static metamodels
import com.pucminas.icoleta.repository.CollectPointRepository;
import com.pucminas.icoleta.service.dto.CollectPointCriteria;
import com.pucminas.icoleta.service.dto.CollectPointDTO;
import com.pucminas.icoleta.service.mapper.CollectPointMapper;

/**
 * Service for executing complex queries for {@link CollectPoint} entities in the database.
 * The main input is a {@link CollectPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollectPointDTO} or a {@link Page} of {@link CollectPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollectPointQueryService extends QueryService<CollectPoint> {

    private final Logger log = LoggerFactory.getLogger(CollectPointQueryService.class);

    private final CollectPointRepository collectPointRepository;

    private final CollectPointMapper collectPointMapper;

    public CollectPointQueryService(CollectPointRepository collectPointRepository, CollectPointMapper collectPointMapper) {
        this.collectPointRepository = collectPointRepository;
        this.collectPointMapper = collectPointMapper;
    }

    /**
     * Return a {@link List} of {@link CollectPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollectPointDTO> findByCriteria(CollectPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CollectPoint> specification = createSpecification(criteria);
        return collectPointMapper.toDto(collectPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollectPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectPointDTO> findByCriteria(CollectPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CollectPoint> specification = createSpecification(criteria);
        return collectPointRepository.findAll(specification, page)
            .map(collectPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollectPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CollectPoint> specification = createSpecification(criteria);
        return collectPointRepository.count(specification);
    }

    /**
     * Function to convert {@link CollectPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CollectPoint> createSpecification(CollectPointCriteria criteria) {
        Specification<CollectPoint> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CollectPoint_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CollectPoint_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CollectPoint_.description));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), CollectPoint_.lat));
            }
            if (criteria.getLon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLon(), CollectPoint_.lon));
            }
            if (criteria.getMaterialsId() != null) {
                if(criteria.getMaterialsId().getIn() != null && !criteria.getMaterialsId().getIn().isEmpty()) {
                    specification = specification.and(((root, query, criteriaBuilder) -> {
                        Join<CollectPoint, Material> join = root.join(CollectPoint_.materials, JoinType.LEFT);
                        query.distinct(true);
                        Predicate p = null;
                        for(Long id : criteria.getMaterialsId().getIn()) {
                            if(p == null) {
                                p = criteriaBuilder.equal(join.get(Material_.id), id);
                            } else {
                                p = criteriaBuilder.or(p, criteriaBuilder.equal(join.get(Material_.id), id));
                            }

                        }
                        return p;
                    }));
                } else {
                    specification = specification.and(buildSpecification(criteria.getMaterialsId(),
                        root -> root.join(CollectPoint_.materials, JoinType.LEFT).get(Material_.id)));
                }
            }
            if (criteria.getMaterialType() != null) {
                specification = specification.and(buildSpecification(criteria.getMaterialType(),
                    root -> root.join(CollectPoint_.materials, JoinType.LEFT).get(Material_.materialType)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(CollectPoint_.users, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getUserLogin() != null) {
                specification = specification.and(buildSpecification(criteria.getUserLogin(),
                    root -> root.join(CollectPoint_.users, JoinType.LEFT).get(User_.login)));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(CollectPoint_.createdBy, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
