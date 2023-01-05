package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.InvoiceLineItem;
import com.retailer.invoice.repository.InvoiceLineItemRepository;
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
 * REST controller for managing {@link com.retailer.invoice.domain.InvoiceLineItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvoiceLineItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineItemResource.class);

    private static final String ENTITY_NAME = "invoiceLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceLineItemRepository invoiceLineItemRepository;

    public InvoiceLineItemResource(InvoiceLineItemRepository invoiceLineItemRepository) {
        this.invoiceLineItemRepository = invoiceLineItemRepository;
    }

    /**
     * {@code POST  /invoice-line-items} : Create a new invoiceLineItem.
     *
     * @param invoiceLineItem the invoiceLineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceLineItem, or with status {@code 400 (Bad Request)} if the invoiceLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-line-items")
    public ResponseEntity<InvoiceLineItem> createInvoiceLineItem(@RequestBody InvoiceLineItem invoiceLineItem) throws URISyntaxException {
        log.debug("REST request to save InvoiceLineItem : {}", invoiceLineItem);
        if (invoiceLineItem.getInvoiceLineItemId() != null) {
            throw new BadRequestAlertException("A new invoiceLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceLineItem result = invoiceLineItemRepository.save(invoiceLineItem);
        return ResponseEntity
            .created(new URI("/api/invoice-line-items/" + result.getInvoiceLineItemId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getInvoiceLineItemId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-line-items/:invoiceLineItemId} : Updates an existing invoiceLineItem.
     *
     * @param invoiceLineItemId the id of the invoiceLineItem to save.
     * @param invoiceLineItem the invoiceLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceLineItem,
     * or with status {@code 400 (Bad Request)} if the invoiceLineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-line-items/{invoiceLineItemId}")
    public ResponseEntity<InvoiceLineItem> updateInvoiceLineItem(
        @PathVariable(value = "invoiceLineItemId", required = false) final Long invoiceLineItemId,
        @RequestBody InvoiceLineItem invoiceLineItem
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceLineItem : {}, {}", invoiceLineItemId, invoiceLineItem);
        if (invoiceLineItem.getInvoiceLineItemId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceLineItemId, invoiceLineItem.getInvoiceLineItemId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceLineItemRepository.existsById(invoiceLineItemId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceLineItem result = invoiceLineItemRepository.save(invoiceLineItem);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceLineItem.getInvoiceLineItemId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-line-items/:invoiceLineItemId} : Partial updates given fields of an existing invoiceLineItem, field will ignore if it is null
     *
     * @param invoiceLineItemId the id of the invoiceLineItem to save.
     * @param invoiceLineItem the invoiceLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceLineItem,
     * or with status {@code 400 (Bad Request)} if the invoiceLineItem is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceLineItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-line-items/{invoiceLineItemId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceLineItem> partialUpdateInvoiceLineItem(
        @PathVariable(value = "invoiceLineItemId", required = false) final Long invoiceLineItemId,
        @RequestBody InvoiceLineItem invoiceLineItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceLineItem partially : {}, {}", invoiceLineItemId, invoiceLineItem);
        if (invoiceLineItem.getInvoiceLineItemId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceLineItemId, invoiceLineItem.getInvoiceLineItemId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceLineItemRepository.existsById(invoiceLineItemId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceLineItem> result = invoiceLineItemRepository
            .findById(invoiceLineItem.getInvoiceLineItemId())
            .map(existingInvoiceLineItem -> {
                if (invoiceLineItem.getArticleType() != null) {
                    existingInvoiceLineItem.setArticleType(invoiceLineItem.getArticleType());
                }
                if (invoiceLineItem.getArticleId() != null) {
                    existingInvoiceLineItem.setArticleId(invoiceLineItem.getArticleId());
                }
                if (invoiceLineItem.getQuantity() != null) {
                    existingInvoiceLineItem.setQuantity(invoiceLineItem.getQuantity());
                }
                if (invoiceLineItem.getAmount() != null) {
                    existingInvoiceLineItem.setAmount(invoiceLineItem.getAmount());
                }
                if (invoiceLineItem.getDiscount() != null) {
                    existingInvoiceLineItem.setDiscount(invoiceLineItem.getDiscount());
                }
                if (invoiceLineItem.getCreatedOn() != null) {
                    existingInvoiceLineItem.setCreatedOn(invoiceLineItem.getCreatedOn());
                }
                if (invoiceLineItem.getUpdatedOn() != null) {
                    existingInvoiceLineItem.setUpdatedOn(invoiceLineItem.getUpdatedOn());
                }
                if (invoiceLineItem.getCreatedBy() != null) {
                    existingInvoiceLineItem.setCreatedBy(invoiceLineItem.getCreatedBy());
                }
                if (invoiceLineItem.getUpdatedBy() != null) {
                    existingInvoiceLineItem.setUpdatedBy(invoiceLineItem.getUpdatedBy());
                }

                return existingInvoiceLineItem;
            })
            .map(invoiceLineItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceLineItem.getInvoiceLineItemId().toString())
        );
    }

    /**
     * {@code GET  /invoice-line-items} : get all the invoiceLineItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceLineItems in body.
     */
    @GetMapping("/invoice-line-items")
    public List<InvoiceLineItem> getAllInvoiceLineItems() {
        log.debug("REST request to get all InvoiceLineItems");
        return invoiceLineItemRepository.findAll();
    }

    /**
     * {@code GET  /invoice-line-items/:id} : get the "id" invoiceLineItem.
     *
     * @param id the id of the invoiceLineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceLineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-line-items/{id}")
    public ResponseEntity<InvoiceLineItem> getInvoiceLineItem(@PathVariable Long id) {
        log.debug("REST request to get InvoiceLineItem : {}", id);
        Optional<InvoiceLineItem> invoiceLineItem = invoiceLineItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoiceLineItem);
    }

    /**
     * {@code DELETE  /invoice-line-items/:id} : delete the "id" invoiceLineItem.
     *
     * @param id the id of the invoiceLineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-line-items/{id}")
    public ResponseEntity<Void> deleteInvoiceLineItem(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceLineItem : {}", id);
        invoiceLineItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
