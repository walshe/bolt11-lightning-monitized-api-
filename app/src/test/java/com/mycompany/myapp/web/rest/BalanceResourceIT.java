package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.service.criteria.BalanceCriteria;
import com.mycompany.myapp.service.dto.BalanceDTO;
import com.mycompany.myapp.service.mapper.BalanceMapper;
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
 * Integration tests for the {@link BalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BalanceResourceIT {

    private static final Long DEFAULT_SATS = 1L;
    private static final Long UPDATED_SATS = 2L;
    private static final Long SMALLER_SATS = 1L - 1L;

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBalanceMockMvc;

    private Balance balance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createEntity(EntityManager em) {
        Balance balance = new Balance().sats(DEFAULT_SATS).updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        balance.setUser(user);
        return balance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createUpdatedEntity(EntityManager em) {
        Balance balance = new Balance().sats(UPDATED_SATS).updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        balance.setUser(user);
        return balance;
    }

    @BeforeEach
    public void initTest() {
        balance = createEntity(em);
    }

    @Test
    @Transactional
    void getAllBalances() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].sats").value(hasItem(DEFAULT_SATS.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get the balance
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, balance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(balance.getId().intValue()))
            .andExpect(jsonPath("$.sats").value(DEFAULT_SATS.intValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getBalancesByIdFiltering() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        Long id = balance.getId();

        defaultBalanceShouldBeFound("id.equals=" + id);
        defaultBalanceShouldNotBeFound("id.notEquals=" + id);

        defaultBalanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBalanceShouldNotBeFound("id.greaterThan=" + id);

        defaultBalanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBalanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats equals to DEFAULT_SATS
        defaultBalanceShouldBeFound("sats.equals=" + DEFAULT_SATS);

        // Get all the balanceList where sats equals to UPDATED_SATS
        defaultBalanceShouldNotBeFound("sats.equals=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats not equals to DEFAULT_SATS
        defaultBalanceShouldNotBeFound("sats.notEquals=" + DEFAULT_SATS);

        // Get all the balanceList where sats not equals to UPDATED_SATS
        defaultBalanceShouldBeFound("sats.notEquals=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsInShouldWork() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats in DEFAULT_SATS or UPDATED_SATS
        defaultBalanceShouldBeFound("sats.in=" + DEFAULT_SATS + "," + UPDATED_SATS);

        // Get all the balanceList where sats equals to UPDATED_SATS
        defaultBalanceShouldNotBeFound("sats.in=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats is not null
        defaultBalanceShouldBeFound("sats.specified=true");

        // Get all the balanceList where sats is null
        defaultBalanceShouldNotBeFound("sats.specified=false");
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats is greater than or equal to DEFAULT_SATS
        defaultBalanceShouldBeFound("sats.greaterThanOrEqual=" + DEFAULT_SATS);

        // Get all the balanceList where sats is greater than or equal to UPDATED_SATS
        defaultBalanceShouldNotBeFound("sats.greaterThanOrEqual=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats is less than or equal to DEFAULT_SATS
        defaultBalanceShouldBeFound("sats.lessThanOrEqual=" + DEFAULT_SATS);

        // Get all the balanceList where sats is less than or equal to SMALLER_SATS
        defaultBalanceShouldNotBeFound("sats.lessThanOrEqual=" + SMALLER_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsLessThanSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats is less than DEFAULT_SATS
        defaultBalanceShouldNotBeFound("sats.lessThan=" + DEFAULT_SATS);

        // Get all the balanceList where sats is less than UPDATED_SATS
        defaultBalanceShouldBeFound("sats.lessThan=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesBySatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where sats is greater than DEFAULT_SATS
        defaultBalanceShouldNotBeFound("sats.greaterThan=" + DEFAULT_SATS);

        // Get all the balanceList where sats is greater than SMALLER_SATS
        defaultBalanceShouldBeFound("sats.greaterThan=" + SMALLER_SATS);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt equals to UPDATED_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the balanceList where updatedAt equals to UPDATED_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt is not null
        defaultBalanceShouldBeFound("updatedAt.specified=true");

        // Get all the balanceList where updatedAt is null
        defaultBalanceShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt is less than UPDATED_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultBalanceShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the balanceList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultBalanceShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBalancesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = balance.getUser();
        balanceRepository.saveAndFlush(balance);
        Long userId = user.getId();

        // Get all the balanceList where user equals to userId
        defaultBalanceShouldBeFound("userId.equals=" + userId);

        // Get all the balanceList where user equals to (userId + 1)
        defaultBalanceShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBalanceShouldBeFound(String filter) throws Exception {
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].sats").value(hasItem(DEFAULT_SATS.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBalanceShouldNotBeFound(String filter) throws Exception {
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBalance() throws Exception {
        // Get the balance
        restBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
