package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.ArticleType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceLineItem.
 */
@Entity
@Table(name = "invoice_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_line_item_id")
    private Long invoiceLineItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type")
    private ArticleType articleType;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "amount")
    private Integer amount;

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

    public Long getInvoiceLineItemId() {
        return this.invoiceLineItemId;
    }

    public InvoiceLineItem invoiceLineItemId(Long invoiceLineItemId) {
        this.setInvoiceLineItemId(invoiceLineItemId);
        return this;
    }

    public void setInvoiceLineItemId(Long invoiceLineItemId) {
        this.invoiceLineItemId = invoiceLineItemId;
    }

    public ArticleType getArticleType() {
        return this.articleType;
    }

    public InvoiceLineItem articleType(ArticleType articleType) {
        this.setArticleType(articleType);
        return this;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    public String getArticleId() {
        return this.articleId;
    }

    public InvoiceLineItem articleId(String articleId) {
        this.setArticleId(articleId);
        return this;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public InvoiceLineItem quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public InvoiceLineItem amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getDiscount() {
        return this.discount;
    }

    public InvoiceLineItem discount(Float discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public InvoiceLineItem createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public InvoiceLineItem updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public InvoiceLineItem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public InvoiceLineItem updatedBy(String updatedBy) {
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

    public InvoiceLineItem invoices(Invoice invoice) {
        this.setInvoices(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceLineItem)) {
            return false;
        }
        return invoiceLineItemId != null && invoiceLineItemId.equals(((InvoiceLineItem) o).invoiceLineItemId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceLineItem{" +
            "invoiceLineItemId=" + getInvoiceLineItemId() +
            ", articleType='" + getArticleType() + "'" +
            ", articleId='" + getArticleId() + "'" +
            ", quantity=" + getQuantity() +
            ", amount=" + getAmount() +
            ", discount=" + getDiscount() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
