package com.pucminas.icoleta.web.rest;

import com.pucminas.icoleta.IColetaApp;
import com.pucminas.icoleta.domain.CollectPoint;
import com.pucminas.icoleta.domain.Material;
import com.pucminas.icoleta.domain.User;
import com.pucminas.icoleta.repository.CollectPointRepository;
import com.pucminas.icoleta.service.CollectPointService;
import com.pucminas.icoleta.service.dto.CollectPointDTO;
import com.pucminas.icoleta.service.mapper.CollectPointMapper;
import com.pucminas.icoleta.service.dto.CollectPointCriteria;
import com.pucminas.icoleta.service.CollectPointQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CollectPointResource} REST controller.
 */
@SpringBootTest(classes = IColetaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CollectPointResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;
    private static final Double SMALLER_LON = 1D - 1D;

    @Autowired
    private CollectPointRepository collectPointRepository;

    @Mock
    private CollectPointRepository collectPointRepositoryMock;

    @Autowired
    private CollectPointMapper collectPointMapper;

    @Mock
    private CollectPointService collectPointServiceMock;

    @Autowired
    private CollectPointService collectPointService;

    @Autowired
    private CollectPointQueryService collectPointQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectPointMockMvc;

    private CollectPoint collectPoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectPoint createEntity(EntityManager em) {
        CollectPoint collectPoint = new CollectPoint()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON);
        return collectPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectPoint createUpdatedEntity(EntityManager em) {
        CollectPoint collectPoint = new CollectPoint()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON);
        return collectPoint;
    }

    @BeforeEach
    public void initTest() {
        collectPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectPoint() throws Exception {
        int databaseSizeBeforeCreate = collectPointRepository.findAll().size();

        // Create the CollectPoint
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);
        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isCreated());

        // Validate the CollectPoint in the database
        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeCreate + 1);
        CollectPoint testCollectPoint = collectPointList.get(collectPointList.size() - 1);
        assertThat(testCollectPoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollectPoint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCollectPoint.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testCollectPoint.getLon()).isEqualTo(DEFAULT_LON);
    }

    @Test
    @Transactional
    public void createCollectPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectPointRepository.findAll().size();

        // Create the CollectPoint with an existing ID
        collectPoint.setId(1L);
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CollectPoint in the database
        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectPointRepository.findAll().size();
        // set the field null
        collectPoint.setName(null);

        // Create the CollectPoint, which fails.
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectPointRepository.findAll().size();
        // set the field null
        collectPoint.setDescription(null);

        // Create the CollectPoint, which fails.
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectPointRepository.findAll().size();
        // set the field null
        collectPoint.setLat(null);

        // Create the CollectPoint, which fails.
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectPointRepository.findAll().size();
        // set the field null
        collectPoint.setLon(null);

        // Create the CollectPoint, which fails.
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        restCollectPointMockMvc.perform(post("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollectPoints() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList
        restCollectPointMockMvc.perform(get("/api/collect-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCollectPointsWithEagerRelationshipsIsEnabled() throws Exception {
        when(collectPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollectPointMockMvc.perform(get("/api/collect-points?eagerload=true"))
            .andExpect(status().isOk());

        verify(collectPointServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCollectPointsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(collectPointServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollectPointMockMvc.perform(get("/api/collect-points?eagerload=true"))
            .andExpect(status().isOk());

        verify(collectPointServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCollectPoint() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get the collectPoint
        restCollectPointMockMvc.perform(get("/api/collect-points/{id}", collectPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collectPoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()));
    }


    @Test
    @Transactional
    public void getCollectPointsByIdFiltering() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        Long id = collectPoint.getId();

        defaultCollectPointShouldBeFound("id.equals=" + id);
        defaultCollectPointShouldNotBeFound("id.notEquals=" + id);

        defaultCollectPointShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollectPointShouldNotBeFound("id.greaterThan=" + id);

        defaultCollectPointShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollectPointShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCollectPointsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name equals to DEFAULT_NAME
        defaultCollectPointShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the collectPointList where name equals to UPDATED_NAME
        defaultCollectPointShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name not equals to DEFAULT_NAME
        defaultCollectPointShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the collectPointList where name not equals to UPDATED_NAME
        defaultCollectPointShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCollectPointShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the collectPointList where name equals to UPDATED_NAME
        defaultCollectPointShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name is not null
        defaultCollectPointShouldBeFound("name.specified=true");

        // Get all the collectPointList where name is null
        defaultCollectPointShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCollectPointsByNameContainsSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name contains DEFAULT_NAME
        defaultCollectPointShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the collectPointList where name contains UPDATED_NAME
        defaultCollectPointShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where name does not contain DEFAULT_NAME
        defaultCollectPointShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the collectPointList where name does not contain UPDATED_NAME
        defaultCollectPointShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCollectPointsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description equals to DEFAULT_DESCRIPTION
        defaultCollectPointShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the collectPointList where description equals to UPDATED_DESCRIPTION
        defaultCollectPointShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description not equals to DEFAULT_DESCRIPTION
        defaultCollectPointShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the collectPointList where description not equals to UPDATED_DESCRIPTION
        defaultCollectPointShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCollectPointShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the collectPointList where description equals to UPDATED_DESCRIPTION
        defaultCollectPointShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description is not null
        defaultCollectPointShouldBeFound("description.specified=true");

        // Get all the collectPointList where description is null
        defaultCollectPointShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCollectPointsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description contains DEFAULT_DESCRIPTION
        defaultCollectPointShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the collectPointList where description contains UPDATED_DESCRIPTION
        defaultCollectPointShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where description does not contain DEFAULT_DESCRIPTION
        defaultCollectPointShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the collectPointList where description does not contain UPDATED_DESCRIPTION
        defaultCollectPointShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCollectPointsByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat equals to DEFAULT_LAT
        defaultCollectPointShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the collectPointList where lat equals to UPDATED_LAT
        defaultCollectPointShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat not equals to DEFAULT_LAT
        defaultCollectPointShouldNotBeFound("lat.notEquals=" + DEFAULT_LAT);

        // Get all the collectPointList where lat not equals to UPDATED_LAT
        defaultCollectPointShouldBeFound("lat.notEquals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsInShouldWork() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultCollectPointShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the collectPointList where lat equals to UPDATED_LAT
        defaultCollectPointShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat is not null
        defaultCollectPointShouldBeFound("lat.specified=true");

        // Get all the collectPointList where lat is null
        defaultCollectPointShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat is greater than or equal to DEFAULT_LAT
        defaultCollectPointShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the collectPointList where lat is greater than or equal to UPDATED_LAT
        defaultCollectPointShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat is less than or equal to DEFAULT_LAT
        defaultCollectPointShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the collectPointList where lat is less than or equal to SMALLER_LAT
        defaultCollectPointShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat is less than DEFAULT_LAT
        defaultCollectPointShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the collectPointList where lat is less than UPDATED_LAT
        defaultCollectPointShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lat is greater than DEFAULT_LAT
        defaultCollectPointShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the collectPointList where lat is greater than SMALLER_LAT
        defaultCollectPointShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }


    @Test
    @Transactional
    public void getAllCollectPointsByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon equals to DEFAULT_LON
        defaultCollectPointShouldBeFound("lon.equals=" + DEFAULT_LON);

        // Get all the collectPointList where lon equals to UPDATED_LON
        defaultCollectPointShouldNotBeFound("lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon not equals to DEFAULT_LON
        defaultCollectPointShouldNotBeFound("lon.notEquals=" + DEFAULT_LON);

        // Get all the collectPointList where lon not equals to UPDATED_LON
        defaultCollectPointShouldBeFound("lon.notEquals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsInShouldWork() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon in DEFAULT_LON or UPDATED_LON
        defaultCollectPointShouldBeFound("lon.in=" + DEFAULT_LON + "," + UPDATED_LON);

        // Get all the collectPointList where lon equals to UPDATED_LON
        defaultCollectPointShouldNotBeFound("lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon is not null
        defaultCollectPointShouldBeFound("lon.specified=true");

        // Get all the collectPointList where lon is null
        defaultCollectPointShouldNotBeFound("lon.specified=false");
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon is greater than or equal to DEFAULT_LON
        defaultCollectPointShouldBeFound("lon.greaterThanOrEqual=" + DEFAULT_LON);

        // Get all the collectPointList where lon is greater than or equal to UPDATED_LON
        defaultCollectPointShouldNotBeFound("lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon is less than or equal to DEFAULT_LON
        defaultCollectPointShouldBeFound("lon.lessThanOrEqual=" + DEFAULT_LON);

        // Get all the collectPointList where lon is less than or equal to SMALLER_LON
        defaultCollectPointShouldNotBeFound("lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon is less than DEFAULT_LON
        defaultCollectPointShouldNotBeFound("lon.lessThan=" + DEFAULT_LON);

        // Get all the collectPointList where lon is less than UPDATED_LON
        defaultCollectPointShouldBeFound("lon.lessThan=" + UPDATED_LON);
    }

    @Test
    @Transactional
    public void getAllCollectPointsByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        // Get all the collectPointList where lon is greater than DEFAULT_LON
        defaultCollectPointShouldNotBeFound("lon.greaterThan=" + DEFAULT_LON);

        // Get all the collectPointList where lon is greater than SMALLER_LON
        defaultCollectPointShouldBeFound("lon.greaterThan=" + SMALLER_LON);
    }


    @Test
    @Transactional
    public void getAllCollectPointsByMaterialsIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);
        Material materials = MaterialResourceIT.createEntity(em);
        em.persist(materials);
        em.flush();
        collectPoint.addMaterials(materials);
        collectPointRepository.saveAndFlush(collectPoint);
        Long materialsId = materials.getId();

        // Get all the collectPointList where materials equals to materialsId
        defaultCollectPointShouldBeFound("materialsId.equals=" + materialsId);

        // Get all the collectPointList where materials equals to materialsId + 1
        defaultCollectPointShouldNotBeFound("materialsId.equals=" + (materialsId + 1));
    }


    @Test
    @Transactional
    public void getAllCollectPointsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);
        User users = UserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        collectPoint.addUsers(users);
        collectPointRepository.saveAndFlush(collectPoint);
        Long usersId = users.getId();

        // Get all the collectPointList where users equals to usersId
        defaultCollectPointShouldBeFound("usersId.equals=" + usersId);

        // Get all the collectPointList where users equals to usersId + 1
        defaultCollectPointShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollectPointShouldBeFound(String filter) throws Exception {
        restCollectPointMockMvc.perform(get("/api/collect-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())));

        // Check, that the count call also returns 1
        restCollectPointMockMvc.perform(get("/api/collect-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollectPointShouldNotBeFound(String filter) throws Exception {
        restCollectPointMockMvc.perform(get("/api/collect-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollectPointMockMvc.perform(get("/api/collect-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCollectPoint() throws Exception {
        // Get the collectPoint
        restCollectPointMockMvc.perform(get("/api/collect-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectPoint() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        int databaseSizeBeforeUpdate = collectPointRepository.findAll().size();

        // Update the collectPoint
        CollectPoint updatedCollectPoint = collectPointRepository.findById(collectPoint.getId()).get();
        // Disconnect from session so that the updates on updatedCollectPoint are not directly saved in db
        em.detach(updatedCollectPoint);
        updatedCollectPoint
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON);
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(updatedCollectPoint);

        restCollectPointMockMvc.perform(put("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isOk());

        // Validate the CollectPoint in the database
        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeUpdate);
        CollectPoint testCollectPoint = collectPointList.get(collectPointList.size() - 1);
        assertThat(testCollectPoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollectPoint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCollectPoint.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCollectPoint.getLon()).isEqualTo(UPDATED_LON);
    }

    @Test
    @Transactional
    public void updateNonExistingCollectPoint() throws Exception {
        int databaseSizeBeforeUpdate = collectPointRepository.findAll().size();

        // Create the CollectPoint
        CollectPointDTO collectPointDTO = collectPointMapper.toDto(collectPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectPointMockMvc.perform(put("/api/collect-points")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collectPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CollectPoint in the database
        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCollectPoint() throws Exception {
        // Initialize the database
        collectPointRepository.saveAndFlush(collectPoint);

        int databaseSizeBeforeDelete = collectPointRepository.findAll().size();

        // Delete the collectPoint
        restCollectPointMockMvc.perform(delete("/api/collect-points/{id}", collectPoint.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollectPoint> collectPointList = collectPointRepository.findAll();
        assertThat(collectPointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
