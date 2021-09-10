package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.service.BalanceQueryService;
import com.mycompany.myapp.service.BalanceService;
import com.mycompany.myapp.service.criteria.BalanceCriteria;
import com.mycompany.myapp.service.dto.BalanceDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Balance}.
 */
@RestController
@RequestMapping("/api")
public class BalanceResource {

    private final Logger log = LoggerFactory.getLogger(BalanceResource.class);

    private final BalanceService balanceService;

    private final BalanceRepository balanceRepository;

    private final BalanceQueryService balanceQueryService;

    public BalanceResource(BalanceService balanceService, BalanceRepository balanceRepository, BalanceQueryService balanceQueryService) {
        this.balanceService = balanceService;
        this.balanceRepository = balanceRepository;
        this.balanceQueryService = balanceQueryService;
    }

    /**
     * {@code GET  /balances} : get all the balances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of balances in body.
     */
    @GetMapping("/balances")
    public ResponseEntity<List<BalanceDTO>> getAllBalances() {
        Page<BalanceDTO> page =  new PageImpl<BalanceDTO>(List.of(balanceService.findByCurrentUser()));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

//    /**
//     * {@code GET  /balances/count} : count all the balances.
//     *
//     * @param criteria the criteria which the requested entities should match.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
//     */
//    @GetMapping("/balances/count")
//    public ResponseEntity<Long> countBalances(BalanceCriteria criteria) {
//        log.debug("REST request to count Balances by criteria: {}", criteria);
//        return ResponseEntity.ok().body(balanceQueryService.countByCriteria(criteria));
//    }

//    /**
//     * {@code GET  /balances/:id} : get the "id" balance.
//     *
//     * @param id the id of the balanceDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the balanceDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/balances/{id}")
//    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long id) {
//        log.debug("REST request to get Balance : {}", id);
//        Optional<BalanceDTO> balanceDTO = balanceService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(balanceDTO);
//    }
}
