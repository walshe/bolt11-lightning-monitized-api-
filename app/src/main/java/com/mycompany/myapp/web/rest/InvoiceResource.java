package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InvoiceRepository;
import com.mycompany.myapp.service.InvoiceQueryService;
import com.mycompany.myapp.service.InvoiceService;
import com.mycompany.myapp.service.criteria.InvoiceCriteria;
import com.mycompany.myapp.service.dto.InvoiceDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Invoice}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceQueryService invoiceQueryService;

    public InvoiceResource(InvoiceService invoiceService, InvoiceRepository invoiceRepository, InvoiceQueryService invoiceQueryService) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceQueryService = invoiceQueryService;
    }

    /**
     * {@code POST  /invoices} : Create a new invoice for current user
     *
     * @param invoiceDTO the invoiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceDTO, or with status {@code 400 (Bad Request)} if the invoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoices")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoiceDTO);
        if (invoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceDTO result = invoiceService.save(invoiceDTO);
        return ResponseEntity
            .created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code GET  /invoices} : get all the invoices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoices in body.
     */
    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(InvoiceCriteria criteria, Pageable pageable) {
        //TODO check only get user invoices
        log.debug("REST request to get Invoices by criteria: {}", criteria);
        Page<InvoiceDTO> page = invoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoices/count} : count all the invoices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/invoices/count")
    public ResponseEntity<Long> countInvoices(InvoiceCriteria criteria) {
        log.debug("REST request to count Invoices by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoices/:id} : get the "id" invoice.
     *
     * @param id the id of the invoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) {
        //TODO check that user owns invoice
        log.debug("REST request to get Invoice : {}", id);
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDTO);
    }

    /**
     * {@code DELETE  /invoices/:id} : delete the "id" invoice.
     *
     * @param id the id of the invoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        //TODO check that user owns invoice
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
