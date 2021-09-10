package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Invoice;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.InvoiceRepository;
import com.mycompany.myapp.service.criteria.InvoiceCriteria;
import com.mycompany.myapp.service.dto.InvoiceDTO;
import com.mycompany.myapp.service.mapper.InvoiceMapper;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_BOLT_INVOICE = "AAAAAAAAAA";
    private static final String UPDATED_BOLT_INVOICE = "BBBBBBBBBB";

    private static final Long DEFAULT_SATS = 1L;
    private static final Long UPDATED_SATS = 2L;
    private static final Long SMALLER_SATS = 1L - 1L;

    private static final Boolean DEFAULT_SETTLED = false;
    private static final Boolean UPDATED_SETTLED = true;

    private static final String DEFAULT_PAID_BY_PUB_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PAID_BY_PUB_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SETTLED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SETTLED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SETTLED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .boltInvoice(DEFAULT_BOLT_INVOICE)
            .sats(DEFAULT_SATS)
            .settled(DEFAULT_SETTLED)
            .paidByPubKey(DEFAULT_PAID_BY_PUB_KEY)
            .createdAt(DEFAULT_CREATED_AT)
            .settledAt(DEFAULT_SETTLED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        invoice.setUser(user);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .boltInvoice(UPDATED_BOLT_INVOICE)
            .sats(UPDATED_SATS)
            .settled(UPDATED_SETTLED)
            .paidByPubKey(UPDATED_PAID_BY_PUB_KEY)
            .createdAt(UPDATED_CREATED_AT)
            .settledAt(UPDATED_SETTLED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        invoice.setUser(user);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getBoltInvoice()).isEqualTo(DEFAULT_BOLT_INVOICE);
        assertThat(testInvoice.getSats()).isEqualTo(DEFAULT_SATS);
        assertThat(testInvoice.getSettled()).isEqualTo(DEFAULT_SETTLED);
        assertThat(testInvoice.getPaidByPubKey()).isEqualTo(DEFAULT_PAID_BY_PUB_KEY);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInvoice.getSettledAt()).isEqualTo(DEFAULT_SETTLED_AT);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBoltInvoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setBoltInvoice(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSatsIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSats(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSettledIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSettled(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCreatedAt(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].boltInvoice").value(hasItem(DEFAULT_BOLT_INVOICE)))
            .andExpect(jsonPath("$.[*].sats").value(hasItem(DEFAULT_SATS.intValue())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].paidByPubKey").value(hasItem(DEFAULT_PAID_BY_PUB_KEY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].settledAt").value(hasItem(DEFAULT_SETTLED_AT.toString())));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.boltInvoice").value(DEFAULT_BOLT_INVOICE))
            .andExpect(jsonPath("$.sats").value(DEFAULT_SATS.intValue()))
            .andExpect(jsonPath("$.settled").value(DEFAULT_SETTLED.booleanValue()))
            .andExpect(jsonPath("$.paidByPubKey").value(DEFAULT_PAID_BY_PUB_KEY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.settledAt").value(DEFAULT_SETTLED_AT.toString()));
    }

    @Test
    @Transactional
    void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceShouldBeFound("id.equals=" + id);
        defaultInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice equals to DEFAULT_BOLT_INVOICE
        defaultInvoiceShouldBeFound("boltInvoice.equals=" + DEFAULT_BOLT_INVOICE);

        // Get all the invoiceList where boltInvoice equals to UPDATED_BOLT_INVOICE
        defaultInvoiceShouldNotBeFound("boltInvoice.equals=" + UPDATED_BOLT_INVOICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice not equals to DEFAULT_BOLT_INVOICE
        defaultInvoiceShouldNotBeFound("boltInvoice.notEquals=" + DEFAULT_BOLT_INVOICE);

        // Get all the invoiceList where boltInvoice not equals to UPDATED_BOLT_INVOICE
        defaultInvoiceShouldBeFound("boltInvoice.notEquals=" + UPDATED_BOLT_INVOICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice in DEFAULT_BOLT_INVOICE or UPDATED_BOLT_INVOICE
        defaultInvoiceShouldBeFound("boltInvoice.in=" + DEFAULT_BOLT_INVOICE + "," + UPDATED_BOLT_INVOICE);

        // Get all the invoiceList where boltInvoice equals to UPDATED_BOLT_INVOICE
        defaultInvoiceShouldNotBeFound("boltInvoice.in=" + UPDATED_BOLT_INVOICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice is not null
        defaultInvoiceShouldBeFound("boltInvoice.specified=true");

        // Get all the invoiceList where boltInvoice is null
        defaultInvoiceShouldNotBeFound("boltInvoice.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice contains DEFAULT_BOLT_INVOICE
        defaultInvoiceShouldBeFound("boltInvoice.contains=" + DEFAULT_BOLT_INVOICE);

        // Get all the invoiceList where boltInvoice contains UPDATED_BOLT_INVOICE
        defaultInvoiceShouldNotBeFound("boltInvoice.contains=" + UPDATED_BOLT_INVOICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByBoltInvoiceNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where boltInvoice does not contain DEFAULT_BOLT_INVOICE
        defaultInvoiceShouldNotBeFound("boltInvoice.doesNotContain=" + DEFAULT_BOLT_INVOICE);

        // Get all the invoiceList where boltInvoice does not contain UPDATED_BOLT_INVOICE
        defaultInvoiceShouldBeFound("boltInvoice.doesNotContain=" + UPDATED_BOLT_INVOICE);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats equals to DEFAULT_SATS
        defaultInvoiceShouldBeFound("sats.equals=" + DEFAULT_SATS);

        // Get all the invoiceList where sats equals to UPDATED_SATS
        defaultInvoiceShouldNotBeFound("sats.equals=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats not equals to DEFAULT_SATS
        defaultInvoiceShouldNotBeFound("sats.notEquals=" + DEFAULT_SATS);

        // Get all the invoiceList where sats not equals to UPDATED_SATS
        defaultInvoiceShouldBeFound("sats.notEquals=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats in DEFAULT_SATS or UPDATED_SATS
        defaultInvoiceShouldBeFound("sats.in=" + DEFAULT_SATS + "," + UPDATED_SATS);

        // Get all the invoiceList where sats equals to UPDATED_SATS
        defaultInvoiceShouldNotBeFound("sats.in=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats is not null
        defaultInvoiceShouldBeFound("sats.specified=true");

        // Get all the invoiceList where sats is null
        defaultInvoiceShouldNotBeFound("sats.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats is greater than or equal to DEFAULT_SATS
        defaultInvoiceShouldBeFound("sats.greaterThanOrEqual=" + DEFAULT_SATS);

        // Get all the invoiceList where sats is greater than or equal to UPDATED_SATS
        defaultInvoiceShouldNotBeFound("sats.greaterThanOrEqual=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats is less than or equal to DEFAULT_SATS
        defaultInvoiceShouldBeFound("sats.lessThanOrEqual=" + DEFAULT_SATS);

        // Get all the invoiceList where sats is less than or equal to SMALLER_SATS
        defaultInvoiceShouldNotBeFound("sats.lessThanOrEqual=" + SMALLER_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats is less than DEFAULT_SATS
        defaultInvoiceShouldNotBeFound("sats.lessThan=" + DEFAULT_SATS);

        // Get all the invoiceList where sats is less than UPDATED_SATS
        defaultInvoiceShouldBeFound("sats.lessThan=" + UPDATED_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where sats is greater than DEFAULT_SATS
        defaultInvoiceShouldNotBeFound("sats.greaterThan=" + DEFAULT_SATS);

        // Get all the invoiceList where sats is greater than SMALLER_SATS
        defaultInvoiceShouldBeFound("sats.greaterThan=" + SMALLER_SATS);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settled equals to DEFAULT_SETTLED
        defaultInvoiceShouldBeFound("settled.equals=" + DEFAULT_SETTLED);

        // Get all the invoiceList where settled equals to UPDATED_SETTLED
        defaultInvoiceShouldNotBeFound("settled.equals=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settled not equals to DEFAULT_SETTLED
        defaultInvoiceShouldNotBeFound("settled.notEquals=" + DEFAULT_SETTLED);

        // Get all the invoiceList where settled not equals to UPDATED_SETTLED
        defaultInvoiceShouldBeFound("settled.notEquals=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settled in DEFAULT_SETTLED or UPDATED_SETTLED
        defaultInvoiceShouldBeFound("settled.in=" + DEFAULT_SETTLED + "," + UPDATED_SETTLED);

        // Get all the invoiceList where settled equals to UPDATED_SETTLED
        defaultInvoiceShouldNotBeFound("settled.in=" + UPDATED_SETTLED);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settled is not null
        defaultInvoiceShouldBeFound("settled.specified=true");

        // Get all the invoiceList where settled is null
        defaultInvoiceShouldNotBeFound("settled.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey equals to DEFAULT_PAID_BY_PUB_KEY
        defaultInvoiceShouldBeFound("paidByPubKey.equals=" + DEFAULT_PAID_BY_PUB_KEY);

        // Get all the invoiceList where paidByPubKey equals to UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldNotBeFound("paidByPubKey.equals=" + UPDATED_PAID_BY_PUB_KEY);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey not equals to DEFAULT_PAID_BY_PUB_KEY
        defaultInvoiceShouldNotBeFound("paidByPubKey.notEquals=" + DEFAULT_PAID_BY_PUB_KEY);

        // Get all the invoiceList where paidByPubKey not equals to UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldBeFound("paidByPubKey.notEquals=" + UPDATED_PAID_BY_PUB_KEY);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey in DEFAULT_PAID_BY_PUB_KEY or UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldBeFound("paidByPubKey.in=" + DEFAULT_PAID_BY_PUB_KEY + "," + UPDATED_PAID_BY_PUB_KEY);

        // Get all the invoiceList where paidByPubKey equals to UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldNotBeFound("paidByPubKey.in=" + UPDATED_PAID_BY_PUB_KEY);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey is not null
        defaultInvoiceShouldBeFound("paidByPubKey.specified=true");

        // Get all the invoiceList where paidByPubKey is null
        defaultInvoiceShouldNotBeFound("paidByPubKey.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey contains DEFAULT_PAID_BY_PUB_KEY
        defaultInvoiceShouldBeFound("paidByPubKey.contains=" + DEFAULT_PAID_BY_PUB_KEY);

        // Get all the invoiceList where paidByPubKey contains UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldNotBeFound("paidByPubKey.contains=" + UPDATED_PAID_BY_PUB_KEY);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaidByPubKeyNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where paidByPubKey does not contain DEFAULT_PAID_BY_PUB_KEY
        defaultInvoiceShouldNotBeFound("paidByPubKey.doesNotContain=" + DEFAULT_PAID_BY_PUB_KEY);

        // Get all the invoiceList where paidByPubKey does not contain UPDATED_PAID_BY_PUB_KEY
        defaultInvoiceShouldBeFound("paidByPubKey.doesNotContain=" + UPDATED_PAID_BY_PUB_KEY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt equals to DEFAULT_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt equals to UPDATED_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt not equals to DEFAULT_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt not equals to UPDATED_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the invoiceList where createdAt equals to UPDATED_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt is not null
        defaultInvoiceShouldBeFound("createdAt.specified=true");

        // Get all the invoiceList where createdAt is null
        defaultInvoiceShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt is less than DEFAULT_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt is less than UPDATED_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where createdAt is greater than DEFAULT_CREATED_AT
        defaultInvoiceShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the invoiceList where createdAt is greater than SMALLER_CREATED_AT
        defaultInvoiceShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt equals to DEFAULT_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.equals=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt equals to UPDATED_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.equals=" + UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt not equals to DEFAULT_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.notEquals=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt not equals to UPDATED_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.notEquals=" + UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt in DEFAULT_SETTLED_AT or UPDATED_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.in=" + DEFAULT_SETTLED_AT + "," + UPDATED_SETTLED_AT);

        // Get all the invoiceList where settledAt equals to UPDATED_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.in=" + UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt is not null
        defaultInvoiceShouldBeFound("settledAt.specified=true");

        // Get all the invoiceList where settledAt is null
        defaultInvoiceShouldNotBeFound("settledAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt is greater than or equal to DEFAULT_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.greaterThanOrEqual=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt is greater than or equal to UPDATED_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.greaterThanOrEqual=" + UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt is less than or equal to DEFAULT_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.lessThanOrEqual=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt is less than or equal to SMALLER_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.lessThanOrEqual=" + SMALLER_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt is less than DEFAULT_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.lessThan=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt is less than UPDATED_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.lessThan=" + UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesBySettledAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where settledAt is greater than DEFAULT_SETTLED_AT
        defaultInvoiceShouldNotBeFound("settledAt.greaterThan=" + DEFAULT_SETTLED_AT);

        // Get all the invoiceList where settledAt is greater than SMALLER_SETTLED_AT
        defaultInvoiceShouldBeFound("settledAt.greaterThan=" + SMALLER_SETTLED_AT);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        invoice.setUser(user);
        invoiceRepository.saveAndFlush(invoice);
        Long userId = user.getId();

        // Get all the invoiceList where user equals to userId
        defaultInvoiceShouldBeFound("userId.equals=" + userId);

        // Get all the invoiceList where user equals to (userId + 1)
        defaultInvoiceShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].boltInvoice").value(hasItem(DEFAULT_BOLT_INVOICE)))
            .andExpect(jsonPath("$.[*].sats").value(hasItem(DEFAULT_SATS.intValue())))
            .andExpect(jsonPath("$.[*].settled").value(hasItem(DEFAULT_SETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].paidByPubKey").value(hasItem(DEFAULT_PAID_BY_PUB_KEY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].settledAt").value(hasItem(DEFAULT_SETTLED_AT.toString())));

        // Check, that the count call also returns 1
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .boltInvoice(UPDATED_BOLT_INVOICE)
            .sats(UPDATED_SATS)
            .settled(UPDATED_SETTLED)
            .paidByPubKey(UPDATED_PAID_BY_PUB_KEY)
            .createdAt(UPDATED_CREATED_AT)
            .settledAt(UPDATED_SETTLED_AT);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getBoltInvoice()).isEqualTo(UPDATED_BOLT_INVOICE);
        assertThat(testInvoice.getSats()).isEqualTo(UPDATED_SATS);
        assertThat(testInvoice.getSettled()).isEqualTo(UPDATED_SETTLED);
        assertThat(testInvoice.getPaidByPubKey()).isEqualTo(UPDATED_PAID_BY_PUB_KEY);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getSettledAt()).isEqualTo(UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice.settled(UPDATED_SETTLED).paidByPubKey(UPDATED_PAID_BY_PUB_KEY).createdAt(UPDATED_CREATED_AT);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getBoltInvoice()).isEqualTo(DEFAULT_BOLT_INVOICE);
        assertThat(testInvoice.getSats()).isEqualTo(DEFAULT_SATS);
        assertThat(testInvoice.getSettled()).isEqualTo(UPDATED_SETTLED);
        assertThat(testInvoice.getPaidByPubKey()).isEqualTo(UPDATED_PAID_BY_PUB_KEY);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getSettledAt()).isEqualTo(DEFAULT_SETTLED_AT);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .boltInvoice(UPDATED_BOLT_INVOICE)
            .sats(UPDATED_SATS)
            .settled(UPDATED_SETTLED)
            .paidByPubKey(UPDATED_PAID_BY_PUB_KEY)
            .createdAt(UPDATED_CREATED_AT)
            .settledAt(UPDATED_SETTLED_AT);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getBoltInvoice()).isEqualTo(UPDATED_BOLT_INVOICE);
        assertThat(testInvoice.getSats()).isEqualTo(UPDATED_SATS);
        assertThat(testInvoice.getSettled()).isEqualTo(UPDATED_SETTLED);
        assertThat(testInvoice.getPaidByPubKey()).isEqualTo(UPDATED_PAID_BY_PUB_KEY);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getSettledAt()).isEqualTo(UPDATED_SETTLED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
