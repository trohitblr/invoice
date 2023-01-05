package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.Invoice;
import com.retailer.invoice.repository.InvoiceRepository;
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
 * REST controller for managing {@link com.retailer.invoice.domain.Invoice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceRepository invoiceRepository;

    public InvoiceResource(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * {@code POST  /invoices} : Create a new invoice.
     *
     * @param invoice the invoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoice, or with status {@code 400 (Bad Request)} if the invoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoices")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getInvoiceId() != null) {
            throw new BadRequestAlertException("A new invoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Invoice result = invoiceRepository.save(invoice);
        return ResponseEntity
            .created(new URI("/api/invoices/" + result.getInvoiceId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getInvoiceId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoices/:invoiceId} : Updates an existing invoice.
     *
     * @param invoiceId the id of the invoice to save.
     * @param invoice the invoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoice,
     * or with status {@code 400 (Bad Request)} if the invoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoices/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
        @PathVariable(value = "invoiceId", required = false) final Long invoiceId,
        @RequestBody Invoice invoice
    ) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}, {}", invoiceId, invoice);
        if (invoice.getInvoiceId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceId, invoice.getInvoiceId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(invoiceId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Invoice result = invoiceRepository.save(invoice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoice.getInvoiceId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoices/:invoiceId} : Partial updates given fields of an existing invoice, field will ignore if it is null
     *
     * @param invoiceId the id of the invoice to save.
     * @param invoice the invoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoice,
     * or with status {@code 400 (Bad Request)} if the invoice is not valid,
     * or with status {@code 404 (Not Found)} if the invoice is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoices/{invoiceId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Invoice> partialUpdateInvoice(
        @PathVariable(value = "invoiceId", required = false) final Long invoiceId,
        @RequestBody Invoice invoice
    ) throws URISyntaxException {
        log.debug("REST request to partial update Invoice partially : {}, {}", invoiceId, invoice);
        if (invoice.getInvoiceId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(invoiceId, invoice.getInvoiceId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(invoiceId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Invoice> result = invoiceRepository
            .findById(invoice.getInvoiceId())
            .map(existingInvoice -> {
                if (invoice.getBillNo() != null) {
                    existingInvoice.setBillNo(invoice.getBillNo());
                }
                if (invoice.getTaxPercentage() != null) {
                    existingInvoice.setTaxPercentage(invoice.getTaxPercentage());
                }
                if (invoice.getPaymentType() != null) {
                    existingInvoice.setPaymentType(invoice.getPaymentType());
                }
                if (invoice.getCreatedOn() != null) {
                    existingInvoice.setCreatedOn(invoice.getCreatedOn());
                }
                if (invoice.getUpdatedOn() != null) {
                    existingInvoice.setUpdatedOn(invoice.getUpdatedOn());
                }
                if (invoice.getCreatedBy() != null) {
                    existingInvoice.setCreatedBy(invoice.getCreatedBy());
                }
                if (invoice.getUpdatedBy() != null) {
                    existingInvoice.setUpdatedBy(invoice.getUpdatedBy());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoice.getInvoiceId().toString())
        );
    }

    /**
     * {@code GET  /invoices} : get all the invoices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoices in body.
     */
    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        log.debug("REST request to get all Invoices");
        return invoiceRepository.findAll();
    }

    /**
     * {@code GET  /invoices/:id} : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoice);
    }

    /**
     * {@code DELETE  /invoices/:id} : delete the "id" invoice.
     *
     * @param id the id of the invoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
