package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MonetizedApiInvocation;
import com.mycompany.myapp.repository.MonetizedApiInvocationRepository;
import com.mycompany.myapp.service.dto.MonetizedApiInvocationDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiInvocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MonetizedApiInvocation}.
 */
@Service
@Transactional
public class MonetizedApiInvocationService {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiInvocationService.class);

    private final MonetizedApiInvocationRepository monetizedApiInvocationRepository;

    private final MonetizedApiInvocationMapper monetizedApiInvocationMapper;

    public MonetizedApiInvocationService(
        MonetizedApiInvocationRepository monetizedApiInvocationRepository,
        MonetizedApiInvocationMapper monetizedApiInvocationMapper
    ) {
        this.monetizedApiInvocationRepository = monetizedApiInvocationRepository;
        this.monetizedApiInvocationMapper = monetizedApiInvocationMapper;
    }

    /**
     * Save a monetizedApiInvocation.
     *
     * @param monetizedApiInvocationDTO the entity to save.
     * @return the persisted entity.
     */
    public MonetizedApiInvocationDTO save(MonetizedApiInvocationDTO monetizedApiInvocationDTO) {
        log.debug("Request to save MonetizedApiInvocation : {}", monetizedApiInvocationDTO);
        MonetizedApiInvocation monetizedApiInvocation = monetizedApiInvocationMapper.toEntity(monetizedApiInvocationDTO);
        monetizedApiInvocation = monetizedApiInvocationRepository.save(monetizedApiInvocation);
        return monetizedApiInvocationMapper.toDto(monetizedApiInvocation);
    }

    /**
     * Partially update a monetizedApiInvocation.
     *
     * @param monetizedApiInvocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonetizedApiInvocationDTO> partialUpdate(MonetizedApiInvocationDTO monetizedApiInvocationDTO) {
        log.debug("Request to partially update MonetizedApiInvocation : {}", monetizedApiInvocationDTO);

        return monetizedApiInvocationRepository
            .findById(monetizedApiInvocationDTO.getId())
            .map(
                existingMonetizedApiInvocation -> {
                    monetizedApiInvocationMapper.partialUpdate(existingMonetizedApiInvocation, monetizedApiInvocationDTO);

                    return existingMonetizedApiInvocation;
                }
            )
            .map(monetizedApiInvocationRepository::save)
            .map(monetizedApiInvocationMapper::toDto);
    }

    /**
     * Get all the monetizedApiInvocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MonetizedApiInvocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonetizedApiInvocations");
        return monetizedApiInvocationRepository.findAll(pageable).map(monetizedApiInvocationMapper::toDto);
    }

    /**
     * Get one monetizedApiInvocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonetizedApiInvocationDTO> findOne(Long id) {
        log.debug("Request to get MonetizedApiInvocation : {}", id);
        return monetizedApiInvocationRepository.findById(id).map(monetizedApiInvocationMapper::toDto);
    }

    /**
     * Delete the monetizedApiInvocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MonetizedApiInvocation : {}", id);
        monetizedApiInvocationRepository.deleteById(id);
    }
}
