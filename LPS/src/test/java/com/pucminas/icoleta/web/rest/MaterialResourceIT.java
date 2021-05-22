package com.pucminas.icoleta.web.rest;

import com.pucminas.icoleta.IColetaApp;
import com.pucminas.icoleta.domain.Material;
import com.pucminas.icoleta.domain.CollectPoint;
import com.pucminas.icoleta.repository.MaterialRepository;
import com.pucminas.icoleta.service.MaterialService;
import com.pucminas.icoleta.service.dto.MaterialDTO;
import com.pucminas.icoleta.service.mapper.MaterialMapper;
import com.pucminas.icoleta.service.dto.MaterialCriteria;
import com.pucminas.icoleta.service.MaterialQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pucminas.icoleta.domain.enumeration.MaterialType;
/**
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@SpringBootTest(classes = IColetaApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final MaterialType DEFAULT_MATERIAL_TYPE = MaterialType.LAMP;
    private static final MaterialType UPDATED_MATERIAL_TYPE = MaterialType.BATTERY;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialQueryService materialQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .name(DEFAULT_NAME)
            .materialType(DEFAULT_MATERIAL_TYPE);
        return material;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .name(UPDATED_NAME)
            .materialType(UPDATED_MATERIAL_TYPE);
        return material;
    }

    @BeforeEach
    public void initTest() {
        material = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterial.getMaterialType()).isEqualTo(DEFAULT_MATERIAL_TYPE);
    }

    @Test
    @Transactional
    public void createMaterialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setName(null);

        // Create the Material, which fails.
        MaterialDTO materialDTO = materialMapper.toDto(material);

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaterialTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setMaterialType(null);

        // Create the Material, which fails.
        MaterialDTO materialDTO = materialMapper.toDto(material);

        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].materialType").value(hasItem(DEFAULT_MATERIAL_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.materialType").value(DEFAULT_MATERIAL_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getMaterialsByIdFiltering() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        Long id = material.getId();

        defaultMaterialShouldBeFound("id.equals=" + id);
        defaultMaterialShouldNotBeFound("id.notEquals=" + id);

        defaultMaterialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaterialShouldNotBeFound("id.greaterThan=" + id);

        defaultMaterialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaterialShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMaterialsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name equals to DEFAULT_NAME
        defaultMaterialShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the materialList where name equals to UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name not equals to DEFAULT_NAME
        defaultMaterialShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the materialList where name not equals to UPDATED_NAME
        defaultMaterialShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMaterialShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the materialList where name equals to UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name is not null
        defaultMaterialShouldBeFound("name.specified=true");

        // Get all the materialList where name is null
        defaultMaterialShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaterialsByNameContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name contains DEFAULT_NAME
        defaultMaterialShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the materialList where name contains UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name does not contain DEFAULT_NAME
        defaultMaterialShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the materialList where name does not contain UPDATED_NAME
        defaultMaterialShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllMaterialsByMaterialTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where materialType equals to DEFAULT_MATERIAL_TYPE
        defaultMaterialShouldBeFound("materialType.equals=" + DEFAULT_MATERIAL_TYPE);

        // Get all the materialList where materialType equals to UPDATED_MATERIAL_TYPE
        defaultMaterialShouldNotBeFound("materialType.equals=" + UPDATED_MATERIAL_TYPE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByMaterialTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where materialType not equals to DEFAULT_MATERIAL_TYPE
        defaultMaterialShouldNotBeFound("materialType.notEquals=" + DEFAULT_MATERIAL_TYPE);

        // Get all the materialList where materialType not equals to UPDATED_MATERIAL_TYPE
        defaultMaterialShouldBeFound("materialType.notEquals=" + UPDATED_MATERIAL_TYPE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByMaterialTypeIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where materialType in DEFAULT_MATERIAL_TYPE or UPDATED_MATERIAL_TYPE
        defaultMaterialShouldBeFound("materialType.in=" + DEFAULT_MATERIAL_TYPE + "," + UPDATED_MATERIAL_TYPE);

        // Get all the materialList where materialType equals to UPDATED_MATERIAL_TYPE
        defaultMaterialShouldNotBeFound("materialType.in=" + UPDATED_MATERIAL_TYPE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByMaterialTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where materialType is not null
        defaultMaterialShouldBeFound("materialType.specified=true");

        // Get all the materialList where materialType is null
        defaultMaterialShouldNotBeFound("materialType.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialsByCollectPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
        CollectPoint collectPoints = CollectPointResourceIT.createEntity(em);
        em.persist(collectPoints);
        em.flush();
        material.addCollectPoints(collectPoints);
        materialRepository.saveAndFlush(material);
        Long collectPointsId = collectPoints.getId();

        // Get all the materialList where collectPoints equals to collectPointsId
        defaultMaterialShouldBeFound("collectPointsId.equals=" + collectPointsId);

        // Get all the materialList where collectPoints equals to collectPointsId + 1
        defaultMaterialShouldNotBeFound("collectPointsId.equals=" + (collectPointsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialShouldBeFound(String filter) throws Exception {
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].materialType").value(hasItem(DEFAULT_MATERIAL_TYPE.toString())));

        // Check, that the count call also returns 1
        restMaterialMockMvc.perform(get("/api/materials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialShouldNotBeFound(String filter) throws Exception {
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialMockMvc.perform(get("/api/materials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).get();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial
            .name(UPDATED_NAME)
            .materialType(UPDATED_MATERIAL_TYPE);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterial.getMaterialType()).isEqualTo(UPDATED_MATERIAL_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Delete the material
        restMaterialMockMvc.perform(delete("/api/materials/{id}", material.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
