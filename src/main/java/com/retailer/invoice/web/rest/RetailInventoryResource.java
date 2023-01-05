package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.RetailInventory;
import com.retailer.invoice.repository.RetailInventoryRepository;
import com.retailer.invoice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.retailer.invoice.domain.RetailInventory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RetailInventoryResource {

    private final Logger log = LoggerFactory.getLogger(RetailInventoryResource.class);

    private static final String ENTITY_NAME = "retailInventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetailInventoryRepository retailInventoryRepository;

    public RetailInventoryResource(RetailInventoryRepository retailInventoryRepository) {
        this.retailInventoryRepository = retailInventoryRepository;
    }

    /**
     * {@code POST  /retail-inventories} : Create a new retailInventory.
     *
     * @param retailInventory the retailInventory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retailInventory, or with status {@code 400 (Bad Request)} if the retailInventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retail-inventories")
    public ResponseEntity<RetailInventory> createRetailInventory(@RequestBody RetailInventory retailInventory) throws URISyntaxException {
        log.debug("REST request to save RetailInventory : {}", retailInventory);
        if (retailInventory.getRetailInventoryId() != null) {
            throw new BadRequestAlertException("A new retailInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetailInventory result = retailInventoryRepository.save(retailInventory);
        return ResponseEntity
            .created(new URI("/api/retail-inventories/" + result.getRetailInventoryId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getRetailInventoryId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retail-inventories/:retailInventoryId} : Updates an existing retailInventory.
     *
     * @param retailInventoryId the id of the retailInventory to save.
     * @param retailInventory the retailInventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailInventory,
     * or with status {@code 400 (Bad Request)} if the retailInventory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retailInventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retail-inventories/{retailInventoryId}")
    public ResponseEntity<RetailInventory> updateRetailInventory(
        @PathVariable(value = "retailInventoryId", required = false) final Long retailInventoryId,
        @RequestBody RetailInventory retailInventory
    ) throws URISyntaxException {
        log.debug("REST request to update RetailInventory : {}, {}", retailInventoryId, retailInventory);
        if (retailInventory.getRetailInventoryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailInventoryId, retailInventory.getRetailInventoryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailInventoryRepository.existsById(retailInventoryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetailInventory result = retailInventoryRepository.save(retailInventory);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailInventory.getRetailInventoryId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /retail-inventories/:retailInventoryId} : Partial updates given fields of an existing retailInventory, field will ignore if it is null
     *
     * @param retailInventoryId the id of the retailInventory to save.
     * @param retailInventory the retailInventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailInventory,
     * or with status {@code 400 (Bad Request)} if the retailInventory is not valid,
     * or with status {@code 404 (Not Found)} if the retailInventory is not found,
     * or with status {@code 500 (Internal Server Error)} if the retailInventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retail-inventories/{retailInventoryId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetailInventory> partialUpdateRetailInventory(
        @PathVariable(value = "retailInventoryId", required = false) final Long retailInventoryId,
        @RequestBody RetailInventory retailInventory
    ) throws URISyntaxException {
        log.debug("REST request to partial update RetailInventory partially : {}, {}", retailInventoryId, retailInventory);
        if (retailInventory.getRetailInventoryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailInventoryId, retailInventory.getRetailInventoryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailInventoryRepository.existsById(retailInventoryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetailInventory> result = retailInventoryRepository
            .findById(retailInventory.getRetailInventoryId())
            .map(existingRetailInventory -> {
                if (retailInventory.getQuantity() != null) {
                    existingRetailInventory.setQuantity(retailInventory.getQuantity());
                }
                if (retailInventory.getAvailableQty() != null) {
                    existingRetailInventory.setAvailableQty(retailInventory.getAvailableQty());
                }
                if (retailInventory.getSoldQty() != null) {
                    existingRetailInventory.setSoldQty(retailInventory.getSoldQty());
                }
                if (retailInventory.getMaxLimit() != null) {
                    existingRetailInventory.setMaxLimit(retailInventory.getMaxLimit());
                }
                if (retailInventory.getMinLimit() != null) {
                    existingRetailInventory.setMinLimit(retailInventory.getMinLimit());
                }
                if (retailInventory.getStatus() != null) {
                    existingRetailInventory.setStatus(retailInventory.getStatus());
                }
                if (retailInventory.getCreatedOn() != null) {
                    existingRetailInventory.setCreatedOn(retailInventory.getCreatedOn());
                }
                if (retailInventory.getUpdatedOn() != null) {
                    existingRetailInventory.setUpdatedOn(retailInventory.getUpdatedOn());
                }
                if (retailInventory.getCreatedBy() != null) {
                    existingRetailInventory.setCreatedBy(retailInventory.getCreatedBy());
                }
                if (retailInventory.getUpdatedBy() != null) {
                    existingRetailInventory.setUpdatedBy(retailInventory.getUpdatedBy());
                }

                return existingRetailInventory;
            })
            .map(retailInventoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailInventory.getRetailInventoryId().toString())
        );
    }

    /**
     * {@code GET  /retail-inventories} : get all the retailInventories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retailInventories in body.
     */
    @GetMapping("/retail-inventories")
    public List<RetailInventory> getAllRetailInventories() {
        log.debug("REST request to get all RetailInventories");
        return retailInventoryRepository.findAll();
    }

    /**
     * {@code GET  /retail-inventories/:id} : get the "id" retailInventory.
     *
     * @param id the id of the retailInventory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retailInventory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retail-inventories/{id}")
    public ResponseEntity<RetailInventory> getRetailInventory(@PathVariable Long id) {
        log.debug("REST request to get RetailInventory : {}", id);
        Optional<RetailInventory> retailInventory = retailInventoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(retailInventory);
    }

    /**
     * {@code DELETE  /retail-inventories/:id} : delete the "id" retailInventory.
     *
     * @param id the id of the retailInventory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retail-inventories/{id}")
    public ResponseEntity<Void> deleteRetailInventory(@PathVariable Long id) {
        log.debug("REST request to delete RetailInventory : {}", id);
        retailInventoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
