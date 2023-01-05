package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.Retailer;
import com.retailer.invoice.repository.RetailerRepository;
import com.retailer.invoice.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.retailer.invoice.domain.Retailer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RetailerResource {

    private final Logger log = LoggerFactory.getLogger(RetailerResource.class);

    private static final String ENTITY_NAME = "retailer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetailerRepository retailerRepository;

    public RetailerResource(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    /**
     * {@code POST  /retailers} : Create a new retailer.
     *
     * @param retailer the retailer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retailer, or with status {@code 400 (Bad Request)} if the retailer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retailers")
    public ResponseEntity<Retailer> createRetailer(@Valid @RequestBody Retailer retailer) throws URISyntaxException {
        log.debug("REST request to save Retailer : {}", retailer);
        if (retailer.getRetailerId() != null) {
            throw new BadRequestAlertException("A new retailer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Retailer result = retailerRepository.save(retailer);
        return ResponseEntity
            .created(new URI("/api/retailers/" + result.getRetailerId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getRetailerId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retailers/:retailerId} : Updates an existing retailer.
     *
     * @param retailerId the id of the retailer to save.
     * @param retailer the retailer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailer,
     * or with status {@code 400 (Bad Request)} if the retailer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retailer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retailers/{retailerId}")
    public ResponseEntity<Retailer> updateRetailer(
        @PathVariable(value = "retailerId", required = false) final Long retailerId,
        @Valid @RequestBody Retailer retailer
    ) throws URISyntaxException {
        log.debug("REST request to update Retailer : {}, {}", retailerId, retailer);
        if (retailer.getRetailerId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailerId, retailer.getRetailerId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerRepository.existsById(retailerId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Retailer result = retailerRepository.save(retailer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailer.getRetailerId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retailers/:retailerId} : Partial updates given fields of an existing retailer, field will ignore if it is null
     *
     * @param retailerId the id of the retailer to save.
     * @param retailer the retailer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailer,
     * or with status {@code 400 (Bad Request)} if the retailer is not valid,
     * or with status {@code 404 (Not Found)} if the retailer is not found,
     * or with status {@code 500 (Internal Server Error)} if the retailer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retailers/{retailerId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Retailer> partialUpdateRetailer(
        @PathVariable(value = "retailerId", required = false) final Long retailerId,
        @NotNull @RequestBody Retailer retailer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Retailer partially : {}, {}", retailerId, retailer);
        if (retailer.getRetailerId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailerId, retailer.getRetailerId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerRepository.existsById(retailerId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Retailer> result = retailerRepository
            .findById(retailer.getRetailerId())
            .map(existingRetailer -> {
                if (retailer.getName() != null) {
                    existingRetailer.setName(retailer.getName());
                }
                if (retailer.getOwner() != null) {
                    existingRetailer.setOwner(retailer.getOwner());
                }
                if (retailer.getPhone() != null) {
                    existingRetailer.setPhone(retailer.getPhone());
                }
                if (retailer.getEmail() != null) {
                    existingRetailer.setEmail(retailer.getEmail());
                }
                if (retailer.getGstNumber() != null) {
                    existingRetailer.setGstNumber(retailer.getGstNumber());
                }
                if (retailer.getStatus() != null) {
                    existingRetailer.setStatus(retailer.getStatus());
                }
                if (retailer.getCreatedOn() != null) {
                    existingRetailer.setCreatedOn(retailer.getCreatedOn());
                }
                if (retailer.getUpdatedOn() != null) {
                    existingRetailer.setUpdatedOn(retailer.getUpdatedOn());
                }
                if (retailer.getCreatedBy() != null) {
                    existingRetailer.setCreatedBy(retailer.getCreatedBy());
                }
                if (retailer.getUpdatedBy() != null) {
                    existingRetailer.setUpdatedBy(retailer.getUpdatedBy());
                }

                return existingRetailer;
            })
            .map(retailerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailer.getRetailerId().toString())
        );
    }

    /**
     * {@code GET  /retailers} : get all the retailers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retailers in body.
     */
    @GetMapping("/retailers")
    public List<Retailer> getAllRetailers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Retailers");
        if (eagerload) {
            return retailerRepository.findAllWithEagerRelationships();
        } else {
            return retailerRepository.findAll();
        }
    }

    /**
     * {@code GET  /retailers/:id} : get the "id" retailer.
     *
     * @param id the id of the retailer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retailer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retailers/{id}")
    public ResponseEntity<Retailer> getRetailer(@PathVariable Long id) {
        log.debug("REST request to get Retailer : {}", id);
        Optional<Retailer> retailer = retailerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(retailer);
    }

    /**
     * {@code DELETE  /retailers/:id} : delete the "id" retailer.
     *
     * @param id the id of the retailer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retailers/{id}")
    public ResponseEntity<Void> deleteRetailer(@PathVariable Long id) {
        log.debug("REST request to delete Retailer : {}", id);
        retailerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
