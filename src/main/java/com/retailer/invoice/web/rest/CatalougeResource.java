package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.Catalouge;
import com.retailer.invoice.repository.CatalougeRepository;
import com.retailer.invoice.service.CatalougeService;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.retailer.invoice.domain.Catalouge}.
 */
@RestController
@RequestMapping("/api")
public class CatalougeResource {

    private final Logger log = LoggerFactory.getLogger(CatalougeResource.class);

    private static final String ENTITY_NAME = "catalouge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalougeService catalougeService;

    private final CatalougeRepository catalougeRepository;

    public CatalougeResource(CatalougeService catalougeService, CatalougeRepository catalougeRepository) {
        this.catalougeService = catalougeService;
        this.catalougeRepository = catalougeRepository;
    }

    /**
     * {@code POST  /catalouges} : Create a new catalouge.
     *
     * @param catalouge the catalouge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalouge, or with status {@code 400 (Bad Request)} if the catalouge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalouges")
    public ResponseEntity<Catalouge> createCatalouge(@RequestBody Catalouge catalouge) throws URISyntaxException {
        log.debug("REST request to save Catalouge : {}", catalouge);
        if (catalouge.getCatalougeId() != null) {
            throw new BadRequestAlertException("A new catalouge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Catalouge result = catalougeService.save(catalouge);
        return ResponseEntity
            .created(new URI("/api/catalouges/" + result.getCatalougeId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getCatalougeId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalouges/:catalougeId} : Updates an existing catalouge.
     *
     * @param catalougeId the id of the catalouge to save.
     * @param catalouge the catalouge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalouge,
     * or with status {@code 400 (Bad Request)} if the catalouge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalouge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalouges/{catalougeId}")
    public ResponseEntity<Catalouge> updateCatalouge(
        @PathVariable(value = "catalougeId", required = false) final Long catalougeId,
        @RequestBody Catalouge catalouge
    ) throws URISyntaxException {
        log.debug("REST request to update Catalouge : {}, {}", catalougeId, catalouge);
        if (catalouge.getCatalougeId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(catalougeId, catalouge.getCatalougeId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalougeRepository.existsById(catalougeId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Catalouge result = catalougeService.update(catalouge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catalouge.getCatalougeId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalouges/:catalougeId} : Partial updates given fields of an existing catalouge, field will ignore if it is null
     *
     * @param catalougeId the id of the catalouge to save.
     * @param catalouge the catalouge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalouge,
     * or with status {@code 400 (Bad Request)} if the catalouge is not valid,
     * or with status {@code 404 (Not Found)} if the catalouge is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalouge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalouges/{catalougeId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Catalouge> partialUpdateCatalouge(
        @PathVariable(value = "catalougeId", required = false) final Long catalougeId,
        @RequestBody Catalouge catalouge
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalouge partially : {}, {}", catalougeId, catalouge);
        if (catalouge.getCatalougeId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(catalougeId, catalouge.getCatalougeId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalougeRepository.existsById(catalougeId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Catalouge> result = catalougeService.partialUpdate(catalouge);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catalouge.getCatalougeId().toString())
        );
    }

    /**
     * {@code GET  /catalouges} : get all the catalouges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalouges in body.
     */
    @GetMapping("/catalouges")
    public List<Catalouge> getAllCatalouges() {
        log.debug("REST request to get all Catalouges");
        return catalougeService.findAll();
    }

    /**
     * {@code GET  /catalouges/:id} : get the "id" catalouge.
     *
     * @param id the id of the catalouge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalouge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalouges/{id}")
    public ResponseEntity<Catalouge> getCatalouge(@PathVariable Long id) {
        log.debug("REST request to get Catalouge : {}", id);
        Optional<Catalouge> catalouge = catalougeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalouge);
    }

    @GetMapping("/catalouges/product/{name}")
    public ResponseEntity<List<Catalouge>> getCatalougeByName(@PathVariable String name) {
        log.debug("REST request to get Catalouge : {}", name);
        Optional<List<Catalouge>> catalouges = catalougeRepository.findByNameStartsWith(name);
        return ResponseUtil.wrapOrNotFound(catalouges);
    }

    /**
     * {@code DELETE  /catalouges/:id} : delete the "id" catalouge.
     *
     * @param id the id of the catalouge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalouges/{id}")
    public ResponseEntity<Void> deleteCatalouge(@PathVariable Long id) {
        log.debug("REST request to delete Catalouge : {}", id);
        catalougeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
