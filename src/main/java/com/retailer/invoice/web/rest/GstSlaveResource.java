package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.GstSlave;
import com.retailer.invoice.repository.GstSlaveRepository;
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
 * REST controller for managing {@link com.retailer.invoice.domain.GstSlave}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GstSlaveResource {

    private final Logger log = LoggerFactory.getLogger(GstSlaveResource.class);

    private static final String ENTITY_NAME = "gstSlave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GstSlaveRepository gstSlaveRepository;

    public GstSlaveResource(GstSlaveRepository gstSlaveRepository) {
        this.gstSlaveRepository = gstSlaveRepository;
    }

    /**
     * {@code POST  /gst-slaves} : Create a new gstSlave.
     *
     * @param gstSlave the gstSlave to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gstSlave, or with status {@code 400 (Bad Request)} if the gstSlave has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gst-slaves")
    public ResponseEntity<GstSlave> createGstSlave(@RequestBody GstSlave gstSlave) throws URISyntaxException {
        log.debug("REST request to save GstSlave : {}", gstSlave);
        if (gstSlave.getGstSlaveId() != null) {
            throw new BadRequestAlertException("A new gstSlave cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GstSlave result = gstSlaveRepository.save(gstSlave);
        return ResponseEntity
            .created(new URI("/api/gst-slaves/" + result.getGstSlaveId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getGstSlaveId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gst-slaves/:gstSlaveId} : Updates an existing gstSlave.
     *
     * @param gstSlaveId the id of the gstSlave to save.
     * @param gstSlave the gstSlave to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gstSlave,
     * or with status {@code 400 (Bad Request)} if the gstSlave is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gstSlave couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gst-slaves/{gstSlaveId}")
    public ResponseEntity<GstSlave> updateGstSlave(
        @PathVariable(value = "gstSlaveId", required = false) final Long gstSlaveId,
        @RequestBody GstSlave gstSlave
    ) throws URISyntaxException {
        log.debug("REST request to update GstSlave : {}, {}", gstSlaveId, gstSlave);
        if (gstSlave.getGstSlaveId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(gstSlaveId, gstSlave.getGstSlaveId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gstSlaveRepository.existsById(gstSlaveId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GstSlave result = gstSlaveRepository.save(gstSlave);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gstSlave.getGstSlaveId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gst-slaves/:gstSlaveId} : Partial updates given fields of an existing gstSlave, field will ignore if it is null
     *
     * @param gstSlaveId the id of the gstSlave to save.
     * @param gstSlave the gstSlave to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gstSlave,
     * or with status {@code 400 (Bad Request)} if the gstSlave is not valid,
     * or with status {@code 404 (Not Found)} if the gstSlave is not found,
     * or with status {@code 500 (Internal Server Error)} if the gstSlave couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gst-slaves/{gstSlaveId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GstSlave> partialUpdateGstSlave(
        @PathVariable(value = "gstSlaveId", required = false) final Long gstSlaveId,
        @RequestBody GstSlave gstSlave
    ) throws URISyntaxException {
        log.debug("REST request to partial update GstSlave partially : {}, {}", gstSlaveId, gstSlave);
        if (gstSlave.getGstSlaveId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(gstSlaveId, gstSlave.getGstSlaveId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gstSlaveRepository.existsById(gstSlaveId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GstSlave> result = gstSlaveRepository
            .findById(gstSlave.getGstSlaveId())
            .map(existingGstSlave -> {
                if (gstSlave.getTax() != null) {
                    existingGstSlave.setTax(gstSlave.getTax());
                }
                if (gstSlave.getTaxPercentage() != null) {
                    existingGstSlave.setTaxPercentage(gstSlave.getTaxPercentage());
                }

                return existingGstSlave;
            })
            .map(gstSlaveRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gstSlave.getGstSlaveId().toString())
        );
    }

    /**
     * {@code GET  /gst-slaves} : get all the gstSlaves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gstSlaves in body.
     */
    @GetMapping("/gst-slaves")
    public List<GstSlave> getAllGstSlaves() {
        log.debug("REST request to get all GstSlaves");
        return gstSlaveRepository.findAll();
    }

    /**
     * {@code GET  /gst-slaves/:id} : get the "id" gstSlave.
     *
     * @param id the id of the gstSlave to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gstSlave, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gst-slaves/{id}")
    public ResponseEntity<GstSlave> getGstSlave(@PathVariable Long id) {
        log.debug("REST request to get GstSlave : {}", id);
        Optional<GstSlave> gstSlave = gstSlaveRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gstSlave);
    }

    /**
     * {@code DELETE  /gst-slaves/:id} : delete the "id" gstSlave.
     *
     * @param id the id of the gstSlave to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gst-slaves/{id}")
    public ResponseEntity<Void> deleteGstSlave(@PathVariable Long id) {
        log.debug("REST request to delete GstSlave : {}", id);
        gstSlaveRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
