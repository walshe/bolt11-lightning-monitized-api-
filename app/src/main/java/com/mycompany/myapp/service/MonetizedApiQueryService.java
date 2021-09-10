package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MonetizedApi;
import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.service.criteria.MonetizedApiCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiMapper;
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
 * Service for executing complex queries for {@link MonetizedApi} entities in the database.
 * The main input is a {@link MonetizedApiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MonetizedApiDTO} or a {@link Page} of {@link MonetizedApiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonetizedApiQueryService extends QueryService<MonetizedApi> {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiQueryService.class);

    private final MonetizedApiRepository monetizedApiRepository;

    private final MonetizedApiMapper monetizedApiMapper;

    public MonetizedApiQueryService(MonetizedApiRepository monetizedApiRepository, MonetizedApiMapper monetizedApiMapper) {
        this.monetizedApiRepository = monetizedApiRepository;
        this.monetizedApiMapper = monetizedApiMapper;
    }

    /**
     * Return a {@link List} of {@link MonetizedApiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MonetizedApiDTO> findByCriteria(MonetizedApiCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MonetizedApi> specification = createSpecification(criteria);
        return monetizedApiMapper.toDto(monetizedApiRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MonetizedApiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonetizedApiDTO> findByCriteria(MonetizedApiCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonetizedApi> specification = createSpecification(criteria);
        return monetizedApiRepository.findAll(specification, page).map(monetizedApiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonetizedApiCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MonetizedApi> specification = createSpecification(criteria);
        return monetizedApiRepository.count(specification);
    }

    /**
     * Function to convert {@link MonetizedApiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonetizedApi> createSpecification(MonetizedApiCriteria criteria) {
        Specification<MonetizedApi> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MonetizedApi_.id));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getMethod(), MonetizedApi_.method));
            }
            if (criteria.getUri() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUri(), MonetizedApi_.uri));
            }
            if (criteria.getPriceInSats() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriceInSats(), MonetizedApi_.priceInSats));
            }
        }
        return specification;
    }
}
