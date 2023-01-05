package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "tax_percentage")
    private String taxPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "invoices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoices" }, allowSetters = true)
    private Set<InvoiceLineItem> invoiceLineItems = new HashSet<>();

    @OneToMany(mappedBy = "invoices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoices" }, allowSetters = true)
    private Set<InvoiceGStLineItem> invoiceGStLineItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "invoiceAddress", "billingAddress", "logo", "invoices", "retailerUsers", "retailInventories", "customers" },
        allowSetters = true
    )
    private Retailer retailers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public Invoice invoiceId(Long invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getBillNo() {
        return this.billNo;
    }

    public Invoice billNo(String billNo) {
        this.setBillNo(billNo);
        return this;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTaxPercentage() {
        return this.taxPercentage;
    }

    public Invoice taxPercentage(String taxPercentage) {
        this.setTaxPercentage(taxPercentage);
        return this;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public Invoice paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Invoice createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Invoice updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Invoice createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Invoice updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<InvoiceLineItem> getInvoiceLineItems() {
        return this.invoiceLineItems;
    }

    public void setInvoiceLineItems(Set<InvoiceLineItem> invoiceLineItems) {
        if (this.invoiceLineItems != null) {
            this.invoiceLineItems.forEach(i -> i.setInvoices(null));
        }
        if (invoiceLineItems != null) {
            invoiceLineItems.forEach(i -> i.setInvoices(this));
        }
        this.invoiceLineItems = invoiceLineItems;
    }

    public Invoice invoiceLineItems(Set<InvoiceLineItem> invoiceLineItems) {
        this.setInvoiceLineItems(invoiceLineItems);
        return this;
    }

    public Invoice addInvoiceLineItem(InvoiceLineItem invoiceLineItem) {
        this.invoiceLineItems.add(invoiceLineItem);
        invoiceLineItem.setInvoices(this);
        return this;
    }

    public Invoice removeInvoiceLineItem(InvoiceLineItem invoiceLineItem) {
        this.invoiceLineItems.remove(invoiceLineItem);
        invoiceLineItem.setInvoices(null);
        return this;
    }

    public Set<InvoiceGStLineItem> getInvoiceGStLineItems() {
        return this.invoiceGStLineItems;
    }

    public void setInvoiceGStLineItems(Set<InvoiceGStLineItem> invoiceGStLineItems) {
        if (this.invoiceGStLineItems != null) {
            this.invoiceGStLineItems.forEach(i -> i.setInvoices(null));
        }
        if (invoiceGStLineItems != null) {
            invoiceGStLineItems.forEach(i -> i.setInvoices(this));
        }
        this.invoiceGStLineItems = invoiceGStLineItems;
    }

    public Invoice invoiceGStLineItems(Set<InvoiceGStLineItem> invoiceGStLineItems) {
        this.setInvoiceGStLineItems(invoiceGStLineItems);
        return this;
    }

    public Invoice addInvoiceGStLineItem(InvoiceGStLineItem invoiceGStLineItem) {
        this.invoiceGStLineItems.add(invoiceGStLineItem);
        invoiceGStLineItem.setInvoices(this);
        return this;
    }

    public Invoice removeInvoiceGStLineItem(InvoiceGStLineItem invoiceGStLineItem) {
        this.invoiceGStLineItems.remove(invoiceGStLineItem);
        invoiceGStLineItem.setInvoices(null);
        return this;
    }

    public Retailer getRetailers() {
        return this.retailers;
    }

    public void setRetailers(Retailer retailer) {
        this.retailers = retailer;
    }

    public Invoice retailers(Retailer retailer) {
        this.setRetailers(retailer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return invoiceId != null && invoiceId.equals(((Invoice) o).invoiceId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "invoiceId=" + getInvoiceId() +
            ", billNo='" + getBillNo() + "'" +
            ", taxPercentage='" + getTaxPercentage() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
