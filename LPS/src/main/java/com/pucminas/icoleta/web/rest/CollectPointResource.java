package com.pucminas.icoleta.web.rest;

import com.pucminas.icoleta.domain.User;
import com.pucminas.icoleta.security.SecurityUtils;
import com.pucminas.icoleta.service.CollectPointService;
import com.pucminas.icoleta.service.UserService;
import com.pucminas.icoleta.web.rest.errors.BadRequestAlertException;
import com.pucminas.icoleta.service.dto.CollectPointDTO;
import com.pucminas.icoleta.service.dto.CollectPointCriteria;
import com.pucminas.icoleta.service.CollectPointQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pucminas.icoleta.domain.CollectPoint}.
 */
@RestController
@RequestMapping("/api")
public class CollectPointResource {

    private final Logger log = LoggerFactory.getLogger(CollectPointResource.class);

    private static final String ENTITY_NAME = "collectPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectPointService collectPointService;

    private final CollectPointQueryService collectPointQueryService;

    private final UserService userService;

    public CollectPointResource(CollectPointService collectPointService,
                                CollectPointQueryService collectPointQueryService,
                                UserService userService
    ) {
        this.collectPointService = collectPointService;
        this.collectPointQueryService = collectPointQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /collect-points} : Create a new collectPoint.
     *
     * @param collectPointDTO the collectPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectPointDTO, or with status {@code 400 (Bad Request)} if the collectPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collect-points")
    public ResponseEntity<CollectPointDTO> createCollectPoint(@Valid @RequestBody CollectPointDTO collectPointDTO) throws URISyntaxException {
        log.debug("REST request to save CollectPoint : {}", collectPointDTO);
        if (collectPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = userService.getCurrentUser();
        collectPointDTO.setCreatedById(user.getId());
        CollectPointDTO result = collectPointService.save(collectPointDTO);
        return ResponseEntity.created(new URI("/api/collect-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collect-points} : Updates an existing collectPoint.
     *
     * @param collectPointDTO the collectPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectPointDTO,
     * or with status {@code 400 (Bad Request)} if the collectPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collect-points")
    public ResponseEntity<CollectPointDTO> updateCollectPoint(@Valid @RequestBody CollectPointDTO collectPointDTO) throws URISyntaxException {
        log.debug("REST request to update CollectPoint : {}", collectPointDTO);
        if (collectPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Long createdById = collectPointService.findOne(collectPointDTO.getId()).get().getCreatedById();
        collectPointDTO.setCreatedById(createdById);
        CollectPointDTO result = collectPointService.save(collectPointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, collectPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /collect-points} : get all the collectPoints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectPoints in body.
     */
    @GetMapping("/collect-points")
    public ResponseEntity<List<CollectPointDTO>> getAllCollectPoints(CollectPointCriteria criteria, Pageable pageable, @RequestParam(value="pageable", required = false) Boolean isPageable) {
        log.debug("REST request to get CollectPoints by criteria: {}", criteria);
        if(isPageable == Boolean.FALSE) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<CollectPointDTO> page = collectPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collect-points/count} : count all the collectPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collect-points/count")
    public ResponseEntity<Long> countCollectPoints(CollectPointCriteria criteria) {
        log.debug("REST request to count CollectPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(collectPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collect-points/:id} : get the "id" collectPoint.
     *
     * @param id the id of the collectPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collect-points/{id}")
    public ResponseEntity<CollectPointDTO> getCollectPoint(@PathVariable Long id) {
        log.debug("REST request to get CollectPoint : {}", id);
        Optional<CollectPointDTO> collectPointDTO = collectPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectPointDTO);
    }


    /**
     * {@code GET  /collect-points} : get all the collectPoints from logged user
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectPoints in body.
     */
    @GetMapping("/collect-points/user")
    public ResponseEntity<List<CollectPointDTO>> getAllCollectPointsLoggedUser() {
        log.debug("REST request to get CollectPoints crated by user");
        List<CollectPointDTO> list = collectPointService.findAllLoggedUser();
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code DELETE  /collect-points/:id} : delete the "id" collectPoint.
     *
     * @param id the id of the collectPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collect-points/{id}")
    public ResponseEntity<Void> deleteCollectPoint(@PathVariable Long id) {
        log.debug("REST request to delete CollectPoint : {}", id);
        collectPointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
