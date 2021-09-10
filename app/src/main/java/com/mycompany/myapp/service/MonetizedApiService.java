package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MonetizedApi;
import com.mycompany.myapp.repository.MonetizedApiRepository;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
import com.mycompany.myapp.service.mapper.MonetizedApiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MonetizedApi}.
 */
@Service
@Transactional
public class MonetizedApiService {

    private final Logger log = LoggerFactory.getLogger(MonetizedApiService.class);

    private final MonetizedApiRepository monetizedApiRepository;

    private final MonetizedApiMapper monetizedApiMapper;

    public MonetizedApiService(MonetizedApiRepository monetizedApiRepository, MonetizedApiMapper monetizedApiMapper) {
        this.monetizedApiRepository = monetizedApiRepository;
        this.monetizedApiMapper = monetizedApiMapper;
    }

    /**
     * Save a monetizedApi.
     *
     * @param monetizedApiDTO the entity to save.
     * @return the persisted entity.
     */
    public MonetizedApiDTO save(MonetizedApiDTO monetizedApiDTO) {
        log.debug("Request to save MonetizedApi : {}", monetizedApiDTO);
        MonetizedApi monetizedApi = monetizedApiMapper.toEntity(monetizedApiDTO);
        monetizedApi = monetizedApiRepository.save(monetizedApi);
        return monetizedApiMapper.toDto(monetizedApi);
    }

    /**
     * Partially update a monetizedApi.
     *
     * @param monetizedApiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonetizedApiDTO> partialUpdate(MonetizedApiDTO monetizedApiDTO) {
        log.debug("Request to partially update MonetizedApi : {}", monetizedApiDTO);

        return monetizedApiRepository
            .findById(monetizedApiDTO.getId())
            .map(
                existingMonetizedApi -> {
                    monetizedApiMapper.partialUpdate(existingMonetizedApi, monetizedApiDTO);

                    return existingMonetizedApi;
                }
            )
            .map(monetizedApiRepository::save)
            .map(monetizedApiMapper::toDto);
    }

    /**
     * Get all the monetizedApis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MonetizedApiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonetizedApis");
        return monetizedApiRepository.findAll(pageable).map(monetizedApiMapper::toDto);
    }

    /**
     * Get one monetizedApi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonetizedApiDTO> findOne(Long id) {
        log.debug("Request to get MonetizedApi : {}", id);
        return monetizedApiRepository.findById(id).map(monetizedApiMapper::toDto);
    }

    /**
     * Delete the monetizedApi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MonetizedApi : {}", id);
        monetizedApiRepository.deleteById(id);
    }
}
