package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MonetizedApiInvocationRepository;
import com.mycompany.myapp.service.MonetizedApiInvocationQueryService;
import com.mycompany.myapp.service.MonetizedApiInvocationService;
import com.mycompany.myapp.service.criteria.MonetizedApiInvocationCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiInvocationDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MonetizedApiInvocation}.
 */
@RestController
@RequestMapping("/api")
public class MonetizedApiInvocationResource {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiInvocationResource.class);

    private static final String ENTITY_NAME = "monetizedApiInvocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonetizedApiInvocationService monetizedApiInvocationService;

    private final MonetizedApiInvocationRepository monetizedApiInvocationRepository;

    private final MonetizedApiInvocationQueryService monetizedApiInvocationQueryService;

    public MonetizedApiInvocationResource(
        MonetizedApiInvocationService monetizedApiInvocationService,
        MonetizedApiInvocationRepository monetizedApiInvocationRepository,
        MonetizedApiInvocationQueryService monetizedApiInvocationQueryService
    ) {
        this.monetizedApiInvocationService = monetizedApiInvocationService;
        this.monetizedApiInvocationRepository = monetizedApiInvocationRepository;
        this.monetizedApiInvocationQueryService = monetizedApiInvocationQueryService;
    }

    /**
     * {@code POST  /monetized-api-invocations} : Create a new monetizedApiInvocation.
     *
     * @param monetizedApiInvocationDTO the monetizedApiInvocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monetizedApiInvocationDTO, or with status {@code 400 (Bad Request)} if the monetizedApiInvocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monetized-api-invocations")
    public ResponseEntity<MonetizedApiInvocationDTO> createMonetizedApiInvocation(
        @Valid @RequestBody MonetizedApiInvocationDTO monetizedApiInvocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MonetizedApiInvocation : {}", monetizedApiInvocationDTO);
        if (monetizedApiInvocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new monetizedApiInvocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonetizedApiInvocationDTO result = monetizedApiInvocationService.save(monetizedApiInvocationDTO);
        return ResponseEntity
            .created(new URI("/api/monetized-api-invocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monetized-api-invocations/:id} : Updates an existing monetizedApiInvocation.
     *
     * @param id the id of the monetizedApiInvocationDTO to save.
     * @param monetizedApiInvocationDTO the monetizedApiInvocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monetizedApiInvocationDTO,
     * or with status {@code 400 (Bad Request)} if the monetizedApiInvocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monetizedApiInvocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monetized-api-invocations/{id}")
    public ResponseEntity<MonetizedApiInvocationDTO> updateMonetizedApiInvocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonetizedApiInvocationDTO monetizedApiInvocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MonetizedApiInvocation : {}, {}", id, monetizedApiInvocationDTO);
        if (monetizedApiInvocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monetizedApiInvocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monetizedApiInvocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MonetizedApiInvocationDTO result = monetizedApiInvocationService.save(monetizedApiInvocationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monetizedApiInvocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /monetized-api-invocations/:id} : Partial updates given fields of an existing monetizedApiInvocation, field will ignore if it is null
     *
     * @param id the id of the monetizedApiInvocationDTO to save.
     * @param monetizedApiInvocationDTO the monetizedApiInvocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monetizedApiInvocationDTO,
     * or with status {@code 400 (Bad Request)} if the monetizedApiInvocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monetizedApiInvocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monetizedApiInvocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monetized-api-invocations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MonetizedApiInvocationDTO> partialUpdateMonetizedApiInvocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonetizedApiInvocationDTO monetizedApiInvocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MonetizedApiInvocation partially : {}, {}", id, monetizedApiInvocationDTO);
        if (monetizedApiInvocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monetizedApiInvocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monetizedApiInvocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonetizedApiInvocationDTO> result = monetizedApiInvocationService.partialUpdate(monetizedApiInvocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monetizedApiInvocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monetized-api-invocations} : get all the monetizedApiInvocations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monetizedApiInvocations in body.
     */
    @GetMapping("/monetized-api-invocations")
    public ResponseEntity<List<MonetizedApiInvocationDTO>> getAllMonetizedApiInvocations(
        MonetizedApiInvocationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get MonetizedApiInvocations by criteria: {}", criteria);
        Page<MonetizedApiInvocationDTO> page = monetizedApiInvocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monetized-api-invocations/count} : count all the monetizedApiInvocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/monetized-api-invocations/count")
    public ResponseEntity<Long> countMonetizedApiInvocations(MonetizedApiInvocationCriteria criteria) {
        log.debug("REST request to count MonetizedApiInvocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(monetizedApiInvocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monetized-api-invocations/:id} : get the "id" monetizedApiInvocation.
     *
     * @param id the id of the monetizedApiInvocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monetizedApiInvocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monetized-api-invocations/{id}")
    public ResponseEntity<MonetizedApiInvocationDTO> getMonetizedApiInvocation(@PathVariable Long id) {
        log.debug("REST request to get MonetizedApiInvocation : {}", id);
        Optional<MonetizedApiInvocationDTO> monetizedApiInvocationDTO = monetizedApiInvocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monetizedApiInvocationDTO);
    }

    /**
     * {@code DELETE  /monetized-api-invocations/:id} : delete the "id" monetizedApiInvocation.
     *
     * @param id the id of the monetizedApiInvocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monetized-api-invocations/{id}")
    public ResponseEntity<Void> deleteMonetizedApiInvocation(@PathVariable Long id) {
        log.debug("REST request to delete MonetizedApiInvocation : {}", id);
        monetizedApiInvocationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
