package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MonetizedApi;
import com.mycompany.myapp.domain.enumeration.Method;
import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.service.criteria.MonetizedApiCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiMapper;
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
 * Integration tests for the {@link MonetizedApiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonetizedApiResourceIT {

    private static final Method DEFAULT_METHOD = Method.GET;
    private static final Method UPDATED_METHOD = Method.POST;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE_IN_SATS = 1L;
    private static final Long UPDATED_PRICE_IN_SATS = 2L;
    private static final Long SMALLER_PRICE_IN_SATS = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/monetized-apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonetizedApiRepository monetizedApiRepository;

    @Autowired
    private MonetizedApiMapper monetizedApiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonetizedApiMockMvc;

    private MonetizedApi monetizedApi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonetizedApi createEntity(EntityManager em) {
        MonetizedApi monetizedApi = new MonetizedApi().method(DEFAULT_METHOD).uri(DEFAULT_URI).priceInSats(DEFAULT_PRICE_IN_SATS);
        return monetizedApi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonetizedApi createUpdatedEntity(EntityManager em) {
        MonetizedApi monetizedApi = new MonetizedApi().method(UPDATED_METHOD).uri(UPDATED_URI).priceInSats(UPDATED_PRICE_IN_SATS);
        return monetizedApi;
    }

    @BeforeEach
    public void initTest() {
        monetizedApi = createEntity(em);
    }

    @Test
    @Transactional
    void createMonetizedApi() throws Exception {
        int databaseSizeBeforeCreate = monetizedApiRepository.findAll().size();
        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);
        restMonetizedApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeCreate + 1);
        MonetizedApi testMonetizedApi = monetizedApiList.get(monetizedApiList.size() - 1);
        assertThat(testMonetizedApi.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testMonetizedApi.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testMonetizedApi.getPriceInSats()).isEqualTo(DEFAULT_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void createMonetizedApiWithExistingId() throws Exception {
        // Create the MonetizedApi with an existing ID
        monetizedApi.setId(1L);
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        int databaseSizeBeforeCreate = monetizedApiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonetizedApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = monetizedApiRepository.findAll().size();
        // set the field null
        monetizedApi.setMethod(null);

        // Create the MonetizedApi, which fails.
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        restMonetizedApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUriIsRequired() throws Exception {
        int databaseSizeBeforeTest = monetizedApiRepository.findAll().size();
        // set the field null
        monetizedApi.setUri(null);

        // Create the MonetizedApi, which fails.
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        restMonetizedApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceInSatsIsRequired() throws Exception {
        int databaseSizeBeforeTest = monetizedApiRepository.findAll().size();
        // set the field null
        monetizedApi.setPriceInSats(null);

        // Create the MonetizedApi, which fails.
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        restMonetizedApiMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonetizedApis() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monetizedApi.getId().intValue())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].priceInSats").value(hasItem(DEFAULT_PRICE_IN_SATS.intValue())));
    }

    @Test
    @Transactional
    void getMonetizedApi() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get the monetizedApi
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL_ID, monetizedApi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monetizedApi.getId().intValue()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI))
            .andExpect(jsonPath("$.priceInSats").value(DEFAULT_PRICE_IN_SATS.intValue()));
    }

    @Test
    @Transactional
    void getMonetizedApisByIdFiltering() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        Long id = monetizedApi.getId();

        defaultMonetizedApiShouldBeFound("id.equals=" + id);
        defaultMonetizedApiShouldNotBeFound("id.notEquals=" + id);

        defaultMonetizedApiShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonetizedApiShouldNotBeFound("id.greaterThan=" + id);

        defaultMonetizedApiShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonetizedApiShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where method equals to DEFAULT_METHOD
        defaultMonetizedApiShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the monetizedApiList where method equals to UPDATED_METHOD
        defaultMonetizedApiShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where method not equals to DEFAULT_METHOD
        defaultMonetizedApiShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the monetizedApiList where method not equals to UPDATED_METHOD
        defaultMonetizedApiShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultMonetizedApiShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the monetizedApiList where method equals to UPDATED_METHOD
        defaultMonetizedApiShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where method is not null
        defaultMonetizedApiShouldBeFound("method.specified=true");

        // Get all the monetizedApiList where method is null
        defaultMonetizedApiShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri equals to DEFAULT_URI
        defaultMonetizedApiShouldBeFound("uri.equals=" + DEFAULT_URI);

        // Get all the monetizedApiList where uri equals to UPDATED_URI
        defaultMonetizedApiShouldNotBeFound("uri.equals=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri not equals to DEFAULT_URI
        defaultMonetizedApiShouldNotBeFound("uri.notEquals=" + DEFAULT_URI);

        // Get all the monetizedApiList where uri not equals to UPDATED_URI
        defaultMonetizedApiShouldBeFound("uri.notEquals=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriIsInShouldWork() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri in DEFAULT_URI or UPDATED_URI
        defaultMonetizedApiShouldBeFound("uri.in=" + DEFAULT_URI + "," + UPDATED_URI);

        // Get all the monetizedApiList where uri equals to UPDATED_URI
        defaultMonetizedApiShouldNotBeFound("uri.in=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriIsNullOrNotNull() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri is not null
        defaultMonetizedApiShouldBeFound("uri.specified=true");

        // Get all the monetizedApiList where uri is null
        defaultMonetizedApiShouldNotBeFound("uri.specified=false");
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriContainsSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri contains DEFAULT_URI
        defaultMonetizedApiShouldBeFound("uri.contains=" + DEFAULT_URI);

        // Get all the monetizedApiList where uri contains UPDATED_URI
        defaultMonetizedApiShouldNotBeFound("uri.contains=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByUriNotContainsSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where uri does not contain DEFAULT_URI
        defaultMonetizedApiShouldNotBeFound("uri.doesNotContain=" + DEFAULT_URI);

        // Get all the monetizedApiList where uri does not contain UPDATED_URI
        defaultMonetizedApiShouldBeFound("uri.doesNotContain=" + UPDATED_URI);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats equals to DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.equals=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats equals to UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.equals=" + UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats not equals to DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.notEquals=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats not equals to UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.notEquals=" + UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsInShouldWork() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats in DEFAULT_PRICE_IN_SATS or UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.in=" + DEFAULT_PRICE_IN_SATS + "," + UPDATED_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats equals to UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.in=" + UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats is not null
        defaultMonetizedApiShouldBeFound("priceInSats.specified=true");

        // Get all the monetizedApiList where priceInSats is null
        defaultMonetizedApiShouldNotBeFound("priceInSats.specified=false");
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats is greater than or equal to DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.greaterThanOrEqual=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats is greater than or equal to UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.greaterThanOrEqual=" + UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats is less than or equal to DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.lessThanOrEqual=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats is less than or equal to SMALLER_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.lessThanOrEqual=" + SMALLER_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsLessThanSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats is less than DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.lessThan=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats is less than UPDATED_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.lessThan=" + UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void getAllMonetizedApisByPriceInSatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        // Get all the monetizedApiList where priceInSats is greater than DEFAULT_PRICE_IN_SATS
        defaultMonetizedApiShouldNotBeFound("priceInSats.greaterThan=" + DEFAULT_PRICE_IN_SATS);

        // Get all the monetizedApiList where priceInSats is greater than SMALLER_PRICE_IN_SATS
        defaultMonetizedApiShouldBeFound("priceInSats.greaterThan=" + SMALLER_PRICE_IN_SATS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonetizedApiShouldBeFound(String filter) throws Exception {
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monetizedApi.getId().intValue())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].priceInSats").value(hasItem(DEFAULT_PRICE_IN_SATS.intValue())));

        // Check, that the count call also returns 1
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonetizedApiShouldNotBeFound(String filter) throws Exception {
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonetizedApiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonetizedApi() throws Exception {
        // Get the monetizedApi
        restMonetizedApiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMonetizedApi() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();

        // Update the monetizedApi
        MonetizedApi updatedMonetizedApi = monetizedApiRepository.findById(monetizedApi.getId()).get();
        // Disconnect from session so that the updates on updatedMonetizedApi are not directly saved in db
        em.detach(updatedMonetizedApi);
        updatedMonetizedApi.method(UPDATED_METHOD).uri(UPDATED_URI).priceInSats(UPDATED_PRICE_IN_SATS);
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(updatedMonetizedApi);

        restMonetizedApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monetizedApiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApi testMonetizedApi = monetizedApiList.get(monetizedApiList.size() - 1);
        assertThat(testMonetizedApi.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testMonetizedApi.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApi.getPriceInSats()).isEqualTo(UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void putNonExistingMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monetizedApiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonetizedApiWithPatch() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();

        // Update the monetizedApi using partial update
        MonetizedApi partialUpdatedMonetizedApi = new MonetizedApi();
        partialUpdatedMonetizedApi.setId(monetizedApi.getId());

        partialUpdatedMonetizedApi.uri(UPDATED_URI);

        restMonetizedApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonetizedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonetizedApi))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApi testMonetizedApi = monetizedApiList.get(monetizedApiList.size() - 1);
        assertThat(testMonetizedApi.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testMonetizedApi.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApi.getPriceInSats()).isEqualTo(DEFAULT_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void fullUpdateMonetizedApiWithPatch() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();

        // Update the monetizedApi using partial update
        MonetizedApi partialUpdatedMonetizedApi = new MonetizedApi();
        partialUpdatedMonetizedApi.setId(monetizedApi.getId());

        partialUpdatedMonetizedApi.method(UPDATED_METHOD).uri(UPDATED_URI).priceInSats(UPDATED_PRICE_IN_SATS);

        restMonetizedApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonetizedApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonetizedApi))
            )
            .andExpect(status().isOk());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
        MonetizedApi testMonetizedApi = monetizedApiList.get(monetizedApiList.size() - 1);
        assertThat(testMonetizedApi.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testMonetizedApi.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testMonetizedApi.getPriceInSats()).isEqualTo(UPDATED_PRICE_IN_SATS);
    }

    @Test
    @Transactional
    void patchNonExistingMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monetizedApiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonetizedApi() throws Exception {
        int databaseSizeBeforeUpdate = monetizedApiRepository.findAll().size();
        monetizedApi.setId(count.incrementAndGet());

        // Create the MonetizedApi
        MonetizedApiDTO monetizedApiDTO = monetizedApiMapper.toDto(monetizedApi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonetizedApiMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monetizedApiDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonetizedApi in the database
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonetizedApi() throws Exception {
        // Initialize the database
        monetizedApiRepository.saveAndFlush(monetizedApi);

        int databaseSizeBeforeDelete = monetizedApiRepository.findAll().size();

        // Delete the monetizedApi
        restMonetizedApiMockMvc
            .perform(delete(ENTITY_API_URL_ID, monetizedApi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonetizedApi> monetizedApiList = monetizedApiRepository.findAll();
        assertThat(monetizedApiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
