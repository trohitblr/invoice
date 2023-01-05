package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.ArticleType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceGStLineItem.
 */
@Entity
@Table(name = "invoice_g_st_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceGStLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_g_st_line_item_id")
    private Long invoiceGStLineItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type")
    private ArticleType articleType;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "hsnsac")
    private String hsnsac;

    @Column(name = "sgst")
    private Float sgst;

    @Column(name = "cgst")
    private Float cgst;

    @Column(name = "sgst_tax_amount")
    private Float sgstTaxAmount;

    @Column(name = "cgst_tax_amount")
    private Float cgstTaxAmount;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "invoiceLineItems", "invoiceGStLineItems", "retailers" }, allowSetters = true)
    private Invoice invoices;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getInvoiceGStLineItemId() {
        return this.invoiceGStLineItemId;
    }

    public InvoiceGStLineItem invoiceGStLineItemId(Long invoiceGStLineItemId) {
        this.setInvoiceGStLineItemId(invoiceGStLineItemId);
        return this;
    }

    public void setInvoiceGStLineItemId(Long invoiceGStLineItemId) {
        this.invoiceGStLineItemId = invoiceGStLineItemId;
    }

    public ArticleType getArticleType() {
        return this.articleType;
    }

    public InvoiceGStLineItem articleType(ArticleType articleType) {
        this.setArticleType(articleType);
        return this;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    public String getArticleId() {
        return this.articleId;
    }

    public InvoiceGStLineItem articleId(String articleId) {
        this.setArticleId(articleId);
        return this;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getHsnsac() {
        return this.hsnsac;
    }

    public InvoiceGStLineItem hsnsac(String hsnsac) {
        this.setHsnsac(hsnsac);
        return this;
    }

    public void setHsnsac(String hsnsac) {
        this.hsnsac = hsnsac;
    }

    public Float getSgst() {
        return this.sgst;
    }

    public InvoiceGStLineItem sgst(Float sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(Float sgst) {
        this.sgst = sgst;
    }

    public Float getCgst() {
        return this.cgst;
    }

    public InvoiceGStLineItem cgst(Float cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(Float cgst) {
        this.cgst = cgst;
    }

    public Float getSgstTaxAmount() {
        return this.sgstTaxAmount;
    }

    public InvoiceGStLineItem sgstTaxAmount(Float sgstTaxAmount) {
        this.setSgstTaxAmount(sgstTaxAmount);
        return this;
    }

    public void setSgstTaxAmount(Float sgstTaxAmount) {
        this.sgstTaxAmount = sgstTaxAmount;
    }

    public Float getCgstTaxAmount() {
        return this.cgstTaxAmount;
    }

    public InvoiceGStLineItem cgstTaxAmount(Float cgstTaxAmount) {
        this.setCgstTaxAmount(cgstTaxAmount);
        return this;
    }

    public void setCgstTaxAmount(Float cgstTaxAmount) {
        this.cgstTaxAmount = cgstTaxAmount;
    }

    public Float getAmount() {
        return this.amount;
    }

    public InvoiceGStLineItem amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getDiscount() {
        return this.discount;
    }

    public InvoiceGStLineItem discount(Float discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public InvoiceGStLineItem createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public InvoiceGStLineItem updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public InvoiceGStLineItem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public InvoiceGStLineItem updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Invoice getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Invoice invoice) {
        this.invoices = invoice;
    }

    public InvoiceGStLineItem invoices(Invoice invoice) {
        this.setInvoices(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceGStLineItem)) {
            return false;
        }
        return invoiceGStLineItemId != null && invoiceGStLineItemId.equals(((InvoiceGStLineItem) o).invoiceGStLineItemId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceGStLineItem{" +
            "invoiceGStLineItemId=" + getInvoiceGStLineItemId() +
            ", articleType='" + getArticleType() + "'" +
            ", articleId='" + getArticleId() + "'" +
            ", hsnsac='" + getHsnsac() + "'" +
            ", sgst=" + getSgst() +
            ", cgst=" + getCgst() +
            ", sgstTaxAmount=" + getSgstTaxAmount() +
            ", cgstTaxAmount=" + getCgstTaxAmount() +
            ", amount=" + getAmount() +
            ", discount=" + getDiscount() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
