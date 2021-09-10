package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.MonetizedApiQueryService;
import com.mycompany.myapp.service.MonetizedApiService;
import com.mycompany.myapp.service.criteria.MonetizedApiCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MonetizedApi}.
 */
@RestController
@RequestMapping("/api")
public class SpecialResource {

    private final Logger log = LoggerFactory.getLogger(SpecialResource.class);

    private static final String ENTITY_NAME = "special";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonetizedApiService monetizedApiService;

    private final MonetizedApiRepository monetizedApiRepository;

    private final MonetizedApiQueryService monetizedApiQueryService;

    public SpecialResource(
        MonetizedApiService monetizedApiService,
        MonetizedApiRepository monetizedApiRepository,
        MonetizedApiQueryService monetizedApiQueryService
    ) {
        this.monetizedApiService = monetizedApiService;
        this.monetizedApiRepository = monetizedApiRepository;
        this.monetizedApiQueryService = monetizedApiQueryService;
    }


    @GetMapping("/special")
    public ResponseEntity<String> getSpecial() {
        return ResponseEntity.ok().body("some special and expensive content!");
    }




}
