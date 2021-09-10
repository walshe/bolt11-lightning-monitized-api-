package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MonetizedApiInvocation;
import com.mycompany.myapp.repository.MonetizedApiInvocationRepository;
import com.mycompany.myapp.service.criteria.MonetizedApiInvocationCriteria;
import com.mycompany.myapp.service.dto.MonetizedApiInvocationDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiInvocationMapper;
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
 * Service for executing complex queries for {@link MonetizedApiInvocation} entities in the database.
 * The main input is a {@link MonetizedApiInvocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MonetizedApiInvocationDTO} or a {@link Page} of {@link MonetizedApiInvocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonetizedApiInvocationQueryService extends QueryService<MonetizedApiInvocation> {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiInvocationQueryService.class);

    private final MonetizedApiInvocationRepository monetizedApiInvocationRepository;

    private final MonetizedApiInvocationMapper monetizedApiInvocationMapper;

    public MonetizedApiInvocationQueryService(
        MonetizedApiInvocationRepository monetizedApiInvocationRepository,
        MonetizedApiInvocationMapper monetizedApiInvocationMapper
    ) {
        this.monetizedApiInvocationRepository = monetizedApiInvocationRepository;
        this.monetizedApiInvocationMapper = monetizedApiInvocationMapper;
    }

    /**
     * Return a {@link List} of {@link MonetizedApiInvocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MonetizedApiInvocationDTO> findByCriteria(MonetizedApiInvocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MonetizedApiInvocation> specification = createSpecification(criteria);
        return monetizedApiInvocationMapper.toDto(monetizedApiInvocationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MonetizedApiInvocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonetizedApiInvocationDTO> findByCriteria(MonetizedApiInvocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonetizedApiInvocation> specification = createSpecification(criteria);
        return monetizedApiInvocationRepository.findAll(specification, page).map(monetizedApiInvocationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonetizedApiInvocationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MonetizedApiInvocation> specification = createSpecification(criteria);
        return monetizedApiInvocationRepository.count(specification);
    }

    /**
     * Function to convert {@link MonetizedApiInvocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonetizedApiInvocation> createSpecification(MonetizedApiInvocationCriteria criteria) {
        Specification<MonetizedApiInvocation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MonetizedApiInvocation_.id));
            }
            if (criteria.getUri() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUri(), MonetizedApiInvocation_.uri));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), MonetizedApiInvocation_.createdAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(MonetizedApiInvocation_.user, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
