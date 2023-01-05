package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.InvoiceGStLineItem;
import com.retailer.invoice.repository.InvoiceGStLineItemRepository;
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
 * REST controller for managing {@link com.retailer.invoice.domain.InvoiceGStLineItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvoiceGStLineItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceGStLineItemResource.class);

    private static final String ENTITY_NAME = "invoiceGStLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceGStLineItemRepository invoiceGStLineItemRepository;

    public InvoiceGStLineItemResource(InvoiceGStLineItemRepository invoiceGStLineItemRepository) {
        this.invoiceGStLineItemRepository = invoiceGStLineItemRepository;
    }

    /**
     * {@code POST  /invoice-g-st-line-items} : Create a new invoiceGStLineItem.
     *
     * @param invoiceGStLineItem the invoiceGStLineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceGStLineItem, or with status {@code 400 (Bad Request)} if the invoiceGStLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-g-st-line-items")
    public ResponseEntity<InvoiceGStLineItem> createInvoiceGStLineItem(@RequestBody InvoiceGStLineItem invoiceGStLineItem)
        throws URISyntaxException {
        log.debug("REST request to save InvoiceGStLineItem : {}", invoiceGStLineItem);
        if (invoiceGStLineItem.getInvoiceGStLineItemId() != null) {
            throw new BadRequestAlertException("A new invoiceGStLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceGStLineItem result = invoiceGStLineItemRepository.save(invoiceGStLineItem);
        return ResponseEntity
            .created(new URI("/api/invoice-g-st-line-items/" + result.getInvoiceGStLineItemId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getInvoiceGStLineItemId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-g-st-line-items/:invoiceGStLineItemId} : Updates an existing invoiceGStLineItem.
     *
     * @param invoiceGStLineItemId the id of the invoiceGStLineItem to save.
     * @param invoiceGStLineItem the invoiceGStLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGStLineItem,
     * or with status {@code 400 (Bad Request)} if the invoiceGStLineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGStLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-g-st-line-items/{invoiceGStLineItemId}")
    public ResponseEntity<InvoiceGStLineItem> updateInvoiceGStLineItem(
        @PathVariable(value = "invoiceGStLineItemId", required = false) final Long invoiceGStLineItemId,
        @RequestBody InvoiceGStLineItem invoiceGStLineItem
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceGStLineItem : {}, {}", invoiceGStLineItemId, invoiceGStLineItem);
        if (invoiceGStLineItem.getInvoiceGStLineItemId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceGStLineItemId, invoiceGStLineItem.getInvoiceGStLineItemId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGStLineItemRepository.existsById(invoiceGStLineItemId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceGStLineItem result = invoiceGStLineItemRepository.save(invoiceGStLineItem);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    invoiceGStLineItem.getInvoiceGStLineItemId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-g-st-line-items/:invoiceGStLineItemId} : Partial updates given fields of an existing invoiceGStLineItem, field will ignore if it is null
     *
     * @param invoiceGStLineItemId the id of the invoiceGStLineItem to save.
     * @param invoiceGStLineItem the invoiceGStLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGStLineItem,
     * or with status {@code 400 (Bad Request)} if the invoiceGStLineItem is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceGStLineItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGStLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/invoice-g-st-line-items/{invoiceGStLineItemId}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<InvoiceGStLineItem> partialUpdateInvoiceGStLineItem(
        @PathVariable(value = "invoiceGStLineItemId", required = false) final Long invoiceGStLineItemId,
        @RequestBody InvoiceGStLineItem invoiceGStLineItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceGStLineItem partially : {}, {}", invoiceGStLineItemId, invoiceGStLineItem);
        if (invoiceGStLineItem.getInvoiceGStLineItemId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceGStLineItemId, invoiceGStLineItem.getInvoiceGStLineItemId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGStLineItemRepository.existsById(invoiceGStLineItemId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceGStLineItem> result = invoiceGStLineItemRepository
            .findById(invoiceGStLineItem.getInvoiceGStLineItemId())
            .map(existingInvoiceGStLineItem -> {
                if (invoiceGStLineItem.getArticleType() != null) {
                    existingInvoiceGStLineItem.setArticleType(invoiceGStLineItem.getArticleType());
                }
                if (invoiceGStLineItem.getArticleId() != null) {
                    existingInvoiceGStLineItem.setArticleId(invoiceGStLineItem.getArticleId());
                }
                if (invoiceGStLineItem.getHsnsac() != null) {
                    existingInvoiceGStLineItem.setHsnsac(invoiceGStLineItem.getHsnsac());
                }
                if (invoiceGStLineItem.getSgst() != null) {
                    existingInvoiceGStLineItem.setSgst(invoiceGStLineItem.getSgst());
                }
                if (invoiceGStLineItem.getCgst() != null) {
                    existingInvoiceGStLineItem.setCgst(invoiceGStLineItem.getCgst());
                }
                if (invoiceGStLineItem.getSgstTaxAmount() != null) {
                    existingInvoiceGStLineItem.setSgstTaxAmount(invoiceGStLineItem.getSgstTaxAmount());
                }
                if (invoiceGStLineItem.getCgstTaxAmount() != null) {
                    existingInvoiceGStLineItem.setCgstTaxAmount(invoiceGStLineItem.getCgstTaxAmount());
                }
                if (invoiceGStLineItem.getAmount() != null) {
                    existingInvoiceGStLineItem.setAmount(invoiceGStLineItem.getAmount());
                }
                if (invoiceGStLineItem.getDiscount() != null) {
                    existingInvoiceGStLineItem.setDiscount(invoiceGStLineItem.getDiscount());
                }
                if (invoiceGStLineItem.getCreatedOn() != null) {
                    existingInvoiceGStLineItem.setCreatedOn(invoiceGStLineItem.getCreatedOn());
                }
                if (invoiceGStLineItem.getUpdatedOn() != null) {
                    existingInvoiceGStLineItem.setUpdatedOn(invoiceGStLineItem.getUpdatedOn());
                }
                if (invoiceGStLineItem.getCreatedBy() != null) {
                    existingInvoiceGStLineItem.setCreatedBy(invoiceGStLineItem.getCreatedBy());
                }
                if (invoiceGStLineItem.getUpdatedBy() != null) {
                    existingInvoiceGStLineItem.setUpdatedBy(invoiceGStLineItem.getUpdatedBy());
                }

                return existingInvoiceGStLineItem;
            })
            .map(invoiceGStLineItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceGStLineItem.getInvoiceGStLineItemId().toString())
        );
    }

    /**
     * {@code GET  /invoice-g-st-line-items} : get all the invoiceGStLineItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceGStLineItems in body.
     */
    @GetMapping("/invoice-g-st-line-items")
    public List<InvoiceGStLineItem> getAllInvoiceGStLineItems() {
        log.debug("REST request to get all InvoiceGStLineItems");
        return invoiceGStLineItemRepository.findAll();
    }

    /**
     * {@code GET  /invoice-g-st-line-items/:id} : get the "id" invoiceGStLineItem.
     *
     * @param id the id of the invoiceGStLineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceGStLineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-g-st-line-items/{id}")
    public ResponseEntity<InvoiceGStLineItem> getInvoiceGStLineItem(@PathVariable Long id) {
        log.debug("REST request to get InvoiceGStLineItem : {}", id);
        Optional<InvoiceGStLineItem> invoiceGStLineItem = invoiceGStLineItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoiceGStLineItem);
    }

    /**
     * {@code DELETE  /invoice-g-st-line-items/:id} : delete the "id" invoiceGStLineItem.
     *
     * @param id the id of the invoiceGStLineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-g-st-line-items/{id}")
    public ResponseEntity<Void> deleteInvoiceGStLineItem(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceGStLineItem : {}", id);
        invoiceGStLineItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
