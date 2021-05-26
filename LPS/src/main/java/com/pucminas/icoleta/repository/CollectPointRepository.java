package com.pucminas.icoleta.repository;

import com.pucminas.icoleta.domain.CollectPoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CollectPoint entity.
 */
@Repository
public interface CollectPointRepository extends JpaRepository<CollectPoint, Long>, JpaSpecificationExecutor<CollectPoint> {

    @Query("select collectPoint from CollectPoint collectPoint where collectPoint.createdBy.login = ?#{principal.username}")
    List<CollectPoint> findByCreatedByIsCurrentUser();

    @Query(value = "select distinct collectPoint from CollectPoint collectPoint left join fetch collectPoint.materials left join fetch collectPoint.users",
        countQuery = "select count(distinct collectPoint) from CollectPoint collectPoint")
    Page<CollectPoint> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct collectPoint from CollectPoint collectPoint left join fetch collectPoint.materials left join fetch collectPoint.users")
    List<CollectPoint> findAllWithEagerRelationships();

    @Query("select collectPoint from CollectPoint collectPoint left join fetch collectPoint.materials left join fetch collectPoint.users where collectPoint.id =:id")
    Optional<CollectPoint> findOneWithEagerRelationships(@Param("id") Long id);
}
