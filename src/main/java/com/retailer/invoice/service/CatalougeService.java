package com.retailer.invoice.service;

import com.retailer.invoice.domain.Catalouge;
import com.retailer.invoice.repository.CatalougeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Catalouge}.
 */
@Service
@Transactional
public class CatalougeService {

    private final Logger log = LoggerFactory.getLogger(CatalougeService.class);

    private final CatalougeRepository catalougeRepository;

    public CatalougeService(CatalougeRepository catalougeRepository) {
        this.catalougeRepository = catalougeRepository;
    }

    /**
     * Save a catalouge.
     *
     * @param catalouge the entity to save.
     * @return the persisted entity.
     */
    public Catalouge save(Catalouge catalouge) {
        log.debug("Request to save Catalouge : {}", catalouge);
        return catalougeRepository.save(catalouge);
    }

    /**
     * Update a catalouge.
     *
     * @param catalouge the entity to save.
     * @return the persisted entity.
     */
    public Catalouge update(Catalouge catalouge) {
        log.debug("Request to update Catalouge : {}", catalouge);
        return catalougeRepository.save(catalouge);
    }

    /**
     * Partially update a catalouge.
     *
     * @param catalouge the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Catalouge> partialUpdate(Catalouge catalouge) {
        log.debug("Request to partially update Catalouge : {}", catalouge);

        return catalougeRepository
            .findById(catalouge.getCatalougeId())
            .map(existingCatalouge -> {
                if (catalouge.getName() != null) {
                    existingCatalouge.setName(catalouge.getName());
                }
                if (catalouge.getCost() != null) {
                    existingCatalouge.setCost(catalouge.getCost());
                }
                if (catalouge.getStatus() != null) {
                    existingCatalouge.setStatus(catalouge.getStatus());
                }
                if (catalouge.getDescription() != null) {
                    existingCatalouge.setDescription(catalouge.getDescription());
                }
                if (catalouge.getHsnNo() != null) {
                    existingCatalouge.setHsnNo(catalouge.getHsnNo());
                }
                if (catalouge.getQuantity() != null) {
                    existingCatalouge.setQuantity(catalouge.getQuantity());
                }
                if (catalouge.getCreatedOn() != null) {
                    existingCatalouge.setCreatedOn(catalouge.getCreatedOn());
                }
                if (catalouge.getUpdatedOn() != null) {
                    existingCatalouge.setUpdatedOn(catalouge.getUpdatedOn());
                }
                if (catalouge.getCreatedBy() != null) {
                    existingCatalouge.setCreatedBy(catalouge.getCreatedBy());
                }
                if (catalouge.getUpdatedBy() != null) {
                    existingCatalouge.setUpdatedBy(catalouge.getUpdatedBy());
                }

                return existingCatalouge;
            })
            .map(catalougeRepository::save);
    }

    /**
     * Get all the catalouges.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Catalouge> findAll() {
        log.debug("Request to get all Catalouges");
        return catalougeRepository.findAll();
    }

    /**
     * Get one catalouge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Catalouge> findOne(Long id) {
        log.debug("Request to get Catalouge : {}", id);
        return catalougeRepository.findById(id);
    }

    /**
     * Delete the catalouge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Catalouge : {}", id);
        catalougeRepository.deleteById(id);
    }
}
