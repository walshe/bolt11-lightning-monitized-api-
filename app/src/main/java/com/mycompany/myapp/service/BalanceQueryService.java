package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.repository.BalanceRepository;
import com.mycompany.myapp.service.criteria.BalanceCriteria;
import com.mycompany.myapp.service.dto.BalanceDTO;
import com.mycompany.myapp.service.mapper.BalanceMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Balance} entities in the database.
 * The main input is a {@link BalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BalanceDTO} or a {@link Page} of {@link BalanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BalanceQueryService extends QueryService<Balance> {

    private final Logger log = LoggerFactory.getLogger(BalanceQueryService.class);

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    public BalanceQueryService(BalanceRepository balanceRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    /**
     * Return a {@link List} of {@link BalanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BalanceDTO> findByCriteria(BalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Balance> specification = createSpecification(criteria);
        return balanceMapper.toDto(balanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BalanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BalanceDTO> findByCriteria(BalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Balance> specification = createSpecification(criteria);
        return balanceRepository.findAll(specification, page).map(balanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Balance> specification = createSpecification(criteria);
        return balanceRepository.count(specification);
    }

    /**
     * Function to convert {@link BalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Balance> createSpecification(BalanceCriteria criteria) {
        Specification<Balance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Balance_.id));
            }
            if (criteria.getSats() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSats(), Balance_.sats));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Balance_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Balance_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
