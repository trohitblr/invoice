package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RetailInventory.
 */
@Entity
@Table(name = "retail_inventory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RetailInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retail_inventory_id")
    private Long retailInventoryId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "available_qty")
    private Integer availableQty;

    @Column(name = "sold_qty")
    private Integer soldQty;

    @Column(name = "max_limit")
    private Integer maxLimit;

    @Column(name = "min_limit")
    private Integer minLimit;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "invoiceAddress", "billingAddress", "logo", "invoices", "retailerUsers", "retailInventories", "customers" },
        allowSetters = true
    )
    private Retailer catalougs;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getRetailInventoryId() {
        return this.retailInventoryId;
    }

    public RetailInventory retailInventoryId(Long retailInventoryId) {
        this.setRetailInventoryId(retailInventoryId);
        return this;
    }

    public void setRetailInventoryId(Long retailInventoryId) {
        this.retailInventoryId = retailInventoryId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public RetailInventory quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailableQty() {
        return this.availableQty;
    }

    public RetailInventory availableQty(Integer availableQty) {
        this.setAvailableQty(availableQty);
        return this;
    }

    public void setAvailableQty(Integer availableQty) {
        this.availableQty = availableQty;
    }

    public Integer getSoldQty() {
        return this.soldQty;
    }

    public RetailInventory soldQty(Integer soldQty) {
        this.setSoldQty(soldQty);
        return this;
    }

    public void setSoldQty(Integer soldQty) {
        this.soldQty = soldQty;
    }

    public Integer getMaxLimit() {
        return this.maxLimit;
    }

    public RetailInventory maxLimit(Integer maxLimit) {
        this.setMaxLimit(maxLimit);
        return this;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Integer getMinLimit() {
        return this.minLimit;
    }

    public RetailInventory minLimit(Integer minLimit) {
        this.setMinLimit(minLimit);
        return this;
    }

    public void setMinLimit(Integer minLimit) {
        this.minLimit = minLimit;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public RetailInventory status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public RetailInventory createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public RetailInventory updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public RetailInventory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public RetailInventory updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Retailer getCatalougs() {
        return this.catalougs;
    }

    public void setCatalougs(Retailer retailer) {
        this.catalougs = retailer;
    }

    public RetailInventory catalougs(Retailer retailer) {
        this.setCatalougs(retailer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetailInventory)) {
            return false;
        }
        return retailInventoryId != null && retailInventoryId.equals(((RetailInventory) o).retailInventoryId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetailInventory{" +
            "retailInventoryId=" + getRetailInventoryId() +
            ", quantity=" + getQuantity() +
            ", availableQty=" + getAvailableQty() +
            ", soldQty=" + getSoldQty() +
            ", maxLimit=" + getMaxLimit() +
            ", minLimit=" + getMinLimit() +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
