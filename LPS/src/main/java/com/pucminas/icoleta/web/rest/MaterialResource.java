package com.pucminas.icoleta.web.rest;

import com.pucminas.icoleta.service.MaterialService;
import com.pucminas.icoleta.web.rest.errors.BadRequestAlertException;
import com.pucminas.icoleta.service.dto.MaterialDTO;
import com.pucminas.icoleta.service.dto.MaterialCriteria;
import com.pucminas.icoleta.service.MaterialQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
 * REST controller for managing {@link com.pucminas.icoleta.domain.Material}.
 */
@RestController
@RequestMapping("/api")
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);

    private static final String ENTITY_NAME = "material";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialService materialService;

    private final MaterialQueryService materialQueryService;

    public MaterialResource(MaterialService materialService, MaterialQueryService materialQueryService) {
        this.materialService = materialService;
        this.materialQueryService = materialQueryService;
    }

    /**
     * {@code POST  /materials} : Create a new material.
     *
     * @param materialDTO the materialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialDTO, or with status {@code 400 (Bad Request)} if the material has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/materials")
    public ResponseEntity<MaterialDTO> createMaterial(@Valid @RequestBody MaterialDTO materialDTO) throws URISyntaxException {
        log.debug("REST request to save Material : {}", materialDTO);
        if (materialDTO.getId() != null) {
            throw new BadRequestAlertException("A new material cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialDTO result = materialService.save(materialDTO);
        return ResponseEntity.created(new URI("/api/materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /materials} : Updates an existing material.
     *
     * @param materialDTO the materialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialDTO,
     * or with status {@code 400 (Bad Request)} if the materialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/materials")
    public ResponseEntity<MaterialDTO> updateMaterial(@Valid @RequestBody MaterialDTO materialDTO) throws URISyntaxException {
        log.debug("REST request to update Material : {}", materialDTO);
        if (materialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialDTO result = materialService.save(materialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /materials} : get all the materials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materials in body.
     */
    @GetMapping("/materials")
    public ResponseEntity<List<MaterialDTO>> getAllMaterials(MaterialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Materials by criteria: {}", criteria);
        Page<MaterialDTO> page = materialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /materials/count} : count all the materials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/materials/count")
    public ResponseEntity<Long> countMaterials(MaterialCriteria criteria) {
        log.debug("REST request to count Materials by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /materials/:id} : get the "id" material.
     *
     * @param id the id of the materialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/materials/{id}")
    public ResponseEntity<MaterialDTO> getMaterial(@PathVariable Long id) {
        log.debug("REST request to get Material : {}", id);
        Optional<MaterialDTO> materialDTO = materialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialDTO);
    }

    /**
     * {@code DELETE  /materials/:id} : delete the "id" material.
     *
     * @param id the id of the materialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/materials/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        log.debug("REST request to delete Material : {}", id);
        materialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
