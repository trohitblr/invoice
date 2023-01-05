package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.RetailerUser;
import com.retailer.invoice.repository.RetailerUserRepository;
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
 * REST controller for managing {@link com.retailer.invoice.domain.RetailerUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RetailerUserResource {

    private final Logger log = LoggerFactory.getLogger(RetailerUserResource.class);

    private static final String ENTITY_NAME = "retailerUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetailerUserRepository retailerUserRepository;

    public RetailerUserResource(RetailerUserRepository retailerUserRepository) {
        this.retailerUserRepository = retailerUserRepository;
    }

    /**
     * {@code POST  /retailer-users} : Create a new retailerUser.
     *
     * @param retailerUser the retailerUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retailerUser, or with status {@code 400 (Bad Request)} if the retailerUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retailer-users")
    public ResponseEntity<RetailerUser> createRetailerUser(@RequestBody RetailerUser retailerUser) throws URISyntaxException {
        log.debug("REST request to save RetailerUser : {}", retailerUser);
        if (retailerUser.getRetailerUserId() != null) {
            throw new BadRequestAlertException("A new retailerUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetailerUser result = retailerUserRepository.save(retailerUser);
        return ResponseEntity
            .created(new URI("/api/retailer-users/" + result.getRetailerUserId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getRetailerUserId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retailer-users/:retailerUserId} : Updates an existing retailerUser.
     *
     * @param retailerUserId the id of the retailerUser to save.
     * @param retailerUser the retailerUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailerUser,
     * or with status {@code 400 (Bad Request)} if the retailerUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retailerUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retailer-users/{retailerUserId}")
    public ResponseEntity<RetailerUser> updateRetailerUser(
        @PathVariable(value = "retailerUserId", required = false) final Long retailerUserId,
        @RequestBody RetailerUser retailerUser
    ) throws URISyntaxException {
        log.debug("REST request to update RetailerUser : {}, {}", retailerUserId, retailerUser);
        if (retailerUser.getRetailerUserId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailerUserId, retailerUser.getRetailerUserId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerUserRepository.existsById(retailerUserId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetailerUser result = retailerUserRepository.save(retailerUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailerUser.getRetailerUserId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retailer-users/:retailerUserId} : Partial updates given fields of an existing retailerUser, field will ignore if it is null
     *
     * @param retailerUserId the id of the retailerUser to save.
     * @param retailerUser the retailerUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailerUser,
     * or with status {@code 400 (Bad Request)} if the retailerUser is not valid,
     * or with status {@code 404 (Not Found)} if the retailerUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the retailerUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retailer-users/{retailerUserId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetailerUser> partialUpdateRetailerUser(
        @PathVariable(value = "retailerUserId", required = false) final Long retailerUserId,
        @RequestBody RetailerUser retailerUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update RetailerUser partially : {}, {}", retailerUserId, retailerUser);
        if (retailerUser.getRetailerUserId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(retailerUserId, retailerUser.getRetailerUserId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerUserRepository.existsById(retailerUserId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetailerUser> result = retailerUserRepository
            .findById(retailerUser.getRetailerUserId())
            .map(existingRetailerUser -> {
                if (retailerUser.getUserId() != null) {
                    existingRetailerUser.setUserId(retailerUser.getUserId());
                }
                if (retailerUser.getPhone() != null) {
                    existingRetailerUser.setPhone(retailerUser.getPhone());
                }
                if (retailerUser.getEmail() != null) {
                    existingRetailerUser.setEmail(retailerUser.getEmail());
                }
                if (retailerUser.getType() != null) {
                    existingRetailerUser.setType(retailerUser.getType());
                }
                if (retailerUser.getEncPassword() != null) {
                    existingRetailerUser.setEncPassword(retailerUser.getEncPassword());
                }
                if (retailerUser.getStatus() != null) {
                    existingRetailerUser.setStatus(retailerUser.getStatus());
                }
                if (retailerUser.getCreatedOn() != null) {
                    existingRetailerUser.setCreatedOn(retailerUser.getCreatedOn());
                }
                if (retailerUser.getUpdatedOn() != null) {
                    existingRetailerUser.setUpdatedOn(retailerUser.getUpdatedOn());
                }
                if (retailerUser.getCreatedBy() != null) {
                    existingRetailerUser.setCreatedBy(retailerUser.getCreatedBy());
                }
                if (retailerUser.getUpdatedBy() != null) {
                    existingRetailerUser.setUpdatedBy(retailerUser.getUpdatedBy());
                }

                return existingRetailerUser;
            })
            .map(retailerUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, retailerUser.getRetailerUserId().toString())
        );
    }

    /**
     * {@code GET  /retailer-users} : get all the retailerUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retailerUsers in body.
     */
    @GetMapping("/retailer-users")
    public List<RetailerUser> getAllRetailerUsers() {
        log.debug("REST request to get all RetailerUsers");
        return retailerUserRepository.findAll();
    }

    /**
     * {@code GET  /retailer-users/:id} : get the "id" retailerUser.
     *
     * @param id the id of the retailerUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retailerUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retailer-users/{id}")
    public ResponseEntity<RetailerUser> getRetailerUser(@PathVariable Long id) {
        log.debug("REST request to get RetailerUser : {}", id);
        Optional<RetailerUser> retailerUser = retailerUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(retailerUser);
    }

    /**
     * {@code DELETE  /retailer-users/:id} : delete the "id" retailerUser.
     *
     * @param id the id of the retailerUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retailer-users/{id}")
    public ResponseEntity<Void> deleteRetailerUser(@PathVariable Long id) {
        log.debug("REST request to delete RetailerUser : {}", id);
        retailerUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
