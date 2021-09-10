package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MonetizedApiInvocation;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MonetizedApiInvocationRepository;
import com.mycompany.myapp.service.criteria.MonetizedApiInvocationCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiInvocationDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiInvocationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MonetizedApiInvocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonetizedApiInvocationResourceIT {

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/monetized-api-invocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonetizedApiInvocationRepository monetizedApiInvocationRepository;

    @Autowired
    private MonetizedApiInvocationMapper monetizedApiInvocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonetizedApiInvocationMockMvc;

    private MonetizedApiInvocation monetizedApiInvocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonetizedApiInvocation createEntity(EntityManager em) {
        MonetizedApiInvocation monetizedApiInvocation = new MonetizedApiInvocation().uri(DEFAULT_URI).createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        monetizedApiInvocation.setUser(user);
        return monetizedApiInvocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonetizedApiInvocation createUpdatedEntity(EntityManager em) {
        MonetizedApiInvocation monetizedApiInvocation = new MonetizedApiInvocation().uri(UPDATED_URI).createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        monetizedApiInvocation.setUser(user);
        return monetizedApiInvocation;
    }

    @BeforeEach
    public void initTest() {
        monetizedApiInvocation = createEntity(em);
    }

    @Test
    @Transactional
    void createMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeCreate = monetizedApiInvocationRepository.findAll().size();
        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);
        restMonetizedApiInvocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeCreate + 1);
        MonetizedApiInvocation testMonetizedApiInvocation = monetizedApiInvocationList.get(monetizedApiInvocationList.size() - 1);
        assertThat(testMonetizedApiInvocation.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testMonetizedApiInvocation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createMonetizedApiInvocationWithExistingId() throws Exception {
        // Create the MonetizedApiInvocation with an existing ID
        monetizedApiInvocation.setId(1L);
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        int databaseSizeBeforeCreate = monetizedApiInvocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonetizedApiInvocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUriIsRequired() throws Exception {
        int databaseSizeBeforeTest = monetizedApiInvocationRepository.findAll().size();
        // set the field null
        monetizedApiInvocation.setUri(null);

        // Create the MonetizedApiInvocation, which fails.
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        restMonetizedApiInvocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = monetizedApiInvocationRepository.findAll().size();
        // set the field null
        monetizedApiInvocation.setCreatedAt(null);

        // Create the MonetizedApiInvocation, which fails.
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        restMonetizedApiInvocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocations() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monetizedApiInvocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMonetizedApiInvocation() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get the monetizedApiInvocation
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL_ID, monetizedApiInvocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monetizedApiInvocation.getId().intValue()))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getMonetizedApiInvocationsByIdFiltering() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        Long id = monetizedApiInvocation.getId();

        defaultMonetizedApiInvocationShouldBeFound("id.equals=" + id);
        defaultMonetizedApiInvocationShouldNotBeFound("id.notEquals=" + id);

        defaultMonetizedApiInvocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonetizedApiInvocationShouldNotBeFound("id.greaterThan=" + id);

        defaultMonetizedApiInvocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonetizedApiInvocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri equals to DEFAULT_URI
        defaultMonetizedApiInvocationShouldBeFound("uri.equals=" + DEFAULT_URI);

        // Get all the monetizedApiInvocationList where uri equals to UPDATED_URI
        defaultMonetizedApiInvocationShouldNotBeFound("uri.equals=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri not equals to DEFAULT_URI
        defaultMonetizedApiInvocationShouldNotBeFound("uri.notEquals=" + DEFAULT_URI);

        // Get all the monetizedApiInvocationList where uri not equals to UPDATED_URI
        defaultMonetizedApiInvocationShouldBeFound("uri.notEquals=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriIsInShouldWork() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri in DEFAULT_URI or UPDATED_URI
        defaultMonetizedApiInvocationShouldBeFound("uri.in=" + DEFAULT_URI + "," + UPDATED_URI);

        // Get all the monetizedApiInvocationList where uri equals to UPDATED_URI
        defaultMonetizedApiInvocationShouldNotBeFound("uri.in=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriIsNullOrNotNull() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri is not null
        defaultMonetizedApiInvocationShouldBeFound("uri.specified=true");

        // Get all the monetizedApiInvocationList where uri is null
        defaultMonetizedApiInvocationShouldNotBeFound("uri.specified=false");
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriContainsSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri contains DEFAULT_URI
        defaultMonetizedApiInvocationShouldBeFound("uri.contains=" + DEFAULT_URI);

        // Get all the monetizedApiInvocationList where uri contains UPDATED_URI
        defaultMonetizedApiInvocationShouldNotBeFound("uri.contains=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUriNotContainsSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where uri does not contain DEFAULT_URI
        defaultMonetizedApiInvocationShouldNotBeFound("uri.doesNotContain=" + DEFAULT_URI);

        // Get all the monetizedApiInvocationList where uri does not contain UPDATED_URI
        defaultMonetizedApiInvocationShouldBeFound("uri.doesNotContain=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt equals to DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt equals to UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt not equals to DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt not equals to UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt equals to UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt is not null
        defaultMonetizedApiInvocationShouldBeFound("createdAt.specified=true");

        // Get all the monetizedApiInvocationList where createdAt is null
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt is less than DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt is less than UPDATED_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        // Get all the monetizedApiInvocationList where createdAt is greater than DEFAULT_CREATED_AT
        defaultMonetizedApiInvocationShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the monetizedApiInvocationList where createdAt is greater than SMALLER_CREATED_AT
        defaultMonetizedApiInvocationShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMonetizedApiInvocationsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        monetizedApiInvocation.setUser(user);
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);
        Long userId = user.getId();

        // Get all the monetizedApiInvocationList where user equals to userId
        defaultMonetizedApiInvocationShouldBeFound("userId.equals=" + userId);

        // Get all the monetizedApiInvocationList where user equals to (userId + 1)
        defaultMonetizedApiInvocationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonetizedApiInvocationShouldBeFound(String filter) throws Exception {
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monetizedApiInvocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonetizedApiInvocationShouldNotBeFound(String filter) throws Exception {
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonetizedApiInvocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonetizedApiInvocation() throws Exception {
        // Get the monetizedApiInvocation
        restMonetizedApiInvocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMonetizedApiInvocation() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();

        // Update the monetizedApiInvocation
        MonetizedApiInvocation updatedMonetizedApiInvocation = monetizedApiInvocationRepository
            .findById(monetizedApiInvocation.getId())
            .get();
        // Disconnect from session so that the updates on updatedMonetizedApiInvocation are not directly saved in db
        em.detach(updatedMonetizedApiInvocation);
        updatedMonetizedApiInvocation.uri(UPDATED_URI).createdAt(UPDATED_CREATED_AT);
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(updatedMonetizedApiInvocation);

        restMonetizedApiInvocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monetizedApiInvocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApiInvocation testMonetizedApiInvocation = monetizedApiInvocationList.get(monetizedApiInvocationList.size() - 1);
        assertThat(testMonetizedApiInvocation.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApiInvocation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monetizedApiInvocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonetizedApiInvocationWithPatch() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();

        // Update the monetizedApiInvocation using partial update
        MonetizedApiInvocation partialUpdatedMonetizedApiInvocation = new MonetizedApiInvocation();
        partialUpdatedMonetizedApiInvocation.setId(monetizedApiInvocation.getId());

        partialUpdatedMonetizedApiInvocation.uri(UPDATED_URI);

        restMonetizedApiInvocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonetizedApiInvocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonetizedApiInvocation))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApiInvocation testMonetizedApiInvocation = monetizedApiInvocationList.get(monetizedApiInvocationList.size() - 1);
        assertThat(testMonetizedApiInvocation.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApiInvocation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMonetizedApiInvocationWithPatch() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();

        // Update the monetizedApiInvocation using partial update
        MonetizedApiInvocation partialUpdatedMonetizedApiInvocation = new MonetizedApiInvocation();
        partialUpdatedMonetizedApiInvocation.setId(monetizedApiInvocation.getId());

        partialUpdatedMonetizedApiInvocation.uri(UPDATED_URI).createdAt(UPDATED_CREATED_AT);

        restMonetizedApiInvocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonetizedApiInvocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonetizedApiInvocation))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApiInvocation testMonetizedApiInvocation = monetizedApiInvocationList.get(monetizedApiInvocationList.size() - 1);
        assertThat(testMonetizedApiInvocation.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApiInvocation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monetizedApiInvocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonetizedApiInvocation() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiInvocationRepository.findAll().size();
        monetizedApiInvocation.setId(count.incrementAndGet());

        // Create the MonetizedApiInvocation
        MonetizedApiInvocationDTO monetizedApiInvocationDTO = monetizedApiInvocationMapper.toDto(monetizedApiInvocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiInvocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiInvocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonetizedApiInvocation in the database
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonetizedApiInvocation() throws Exception {
        // Initialize the database
        monetizedApiInvocationRepository.saveAndFlush(monetizedApiInvocation);

        int databaseSizeBeforeDelete = monetizedApiInvocationRepository.findAll().size();

        // Delete the monetizedApiInvocation
        restMonetizedApiInvocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, monetizedApiInvocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonetizedApiInvocation> monetizedApiInvocationList = monetizedApiInvocationRepository.findAll();
        assertThat(monetizedApiInvocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
