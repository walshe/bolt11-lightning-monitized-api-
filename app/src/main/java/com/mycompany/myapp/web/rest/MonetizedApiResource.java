package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.MonetizedApiQueryService;
import com.mycompany.myapp.service.MonetizedApiService;
import com.mycompany.myapp.service.criteria.MonetizedApiCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MonetizedApi}.
 */
@RestController
@RequestMapping("/api")
public class MonetizedApiResource {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiResource.class);

    private static final String ENTITY_NAME = "monetizedApi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonetizedApiService monetizedApiService;

    private final MonetizedApiRepository monetizedApiRepository;

    private final MonetizedApiQueryService monetizedApiQueryService;

    public MonetizedApiResource(
        MonetizedApiService monetizedApiService,
        MonetizedApiRepository monetizedApiRepository,
        MonetizedApiQueryService monetizedApiQueryService
    ) {
        this.monetizedApiService = monetizedApiService;
        this.monetizedApiRepository = monetizedApiRepository;
        this.monetizedApiQueryService = monetizedApiQueryService;
    }

    /**
     * {@code POST  /monetized-apis} : Create a new monetizedApi.
     *
     * @param monetizedApiDTO the monetizedApiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monetizedApiDTO, or with status {@code 400 (Bad Request)} if the monetizedApi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monetized-apis")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<MonetizedApiDTO> createMonetizedApi(@Valid @RequestBody MonetizedApiDTO monetizedApiDTO)
        throws URISyntaxException {
        log.debug("REST request to save MonetizedApi : {}", monetizedApiDTO);
        if (monetizedApiDTO.getId() != null) {
            throw new BadRequestAlertException("A new monetizedApi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonetizedApiDTO result = monetizedApiService.save(monetizedApiDTO);
        return ResponseEntity
            .created(new URI("/api/monetized-apis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monetized-apis/:id} : Updates an existing monetizedApi.
     *
     * @param id the id of the monetizedApiDTO to save.
     * @param monetizedApiDTO the monetizedApiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monetizedApiDTO,
     * or with status {@code 400 (Bad Request)} if the monetizedApiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monetizedApiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monetized-apis/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<MonetizedApiDTO> updateMonetizedApi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonetizedApiDTO monetizedApiDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MonetizedApi : {}, {}", id, monetizedApiDTO);
        if (monetizedApiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monetizedApiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monetizedApiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MonetizedApiDTO result = monetizedApiService.save(monetizedApiDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monetizedApiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /monetized-apis/:id} : Partial updates given fields of an existing monetizedApi, field will ignore if it is null
     *
     * @param id the id of the monetizedApiDTO to save.
     * @param monetizedApiDTO the monetizedApiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monetizedApiDTO,
     * or with status {@code 400 (Bad Request)} if the monetizedApiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monetizedApiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monetizedApiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monetized-apis/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<MonetizedApiDTO> partialUpdateMonetizedApi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonetizedApiDTO monetizedApiDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MonetizedApi partially : {}, {}", id, monetizedApiDTO);
        if (monetizedApiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monetizedApiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monetizedApiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonetizedApiDTO> result = monetizedApiService.partialUpdate(monetizedApiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monetizedApiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monetized-apis} : get all the monetizedApis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monetizedApis in body.
     */
    @GetMapping("/monetized-apis")
    public ResponseEntity<List<MonetizedApiDTO>> getAllMonetizedApis(MonetizedApiCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MonetizedApis by criteria: {}", criteria);
        Page<MonetizedApiDTO> page = monetizedApiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monetized-apis/count} : count all the monetizedApis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/monetized-apis/count")
    public ResponseEntity<Long> countMonetizedApis(MonetizedApiCriteria criteria) {
        log.debug("REST request to count MonetizedApis by criteria: {}", criteria);
        return ResponseEntity.ok().body(monetizedApiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monetized-apis/:id} : get the "id" monetizedApi.
     *
     * @param id the id of the monetizedApiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monetizedApiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monetized-apis/{id}")
    public ResponseEntity<MonetizedApiDTO> getMonetizedApi(@PathVariable Long id) {
        log.debug("REST request to get MonetizedApi : {}", id);
        Optional<MonetizedApiDTO> monetizedApiDTO = monetizedApiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monetizedApiDTO);
    }

    /**
     * {@code DELETE  /monetized-apis/:id} : delete the "id" monetizedApi.
     *
     * @param id the id of the monetizedApiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monetized-apis/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteMonetizedApi(@PathVariable Long id) {
        log.debug("REST request to delete MonetizedApi : {}", id);
        monetizedApiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
