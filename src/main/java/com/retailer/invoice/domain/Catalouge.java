package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Catalouge.
 */
@Entity
@Table(name = "catalouge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalouge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalouge_id")
    private Long catalougeId;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "description")
    private String description;

    @Column(name = "hsn_no")
    private Long hsnNo;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "catalouges" }, allowSetters = true)
    private Categories categorie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "catalouges" }, allowSetters = true)
    private GstSlave taxSlaves;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getCatalougeId() {
        return this.catalougeId;
    }

    public Catalouge catalougeId(Long catalougeId) {
        this.setCatalougeId(catalougeId);
        return this;
    }

    public void setCatalougeId(Long catalougeId) {
        this.catalougeId = catalougeId;
    }

    public String getName() {
        return this.name;
    }

    public Catalouge name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return this.cost;
    }

    public Catalouge cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Catalouge status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public Catalouge description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getHsnNo() {
        return this.hsnNo;
    }

    public Catalouge hsnNo(Long hsnNo) {
        this.setHsnNo(hsnNo);
        return this;
    }

    public void setHsnNo(Long hsnNo) {
        this.hsnNo = hsnNo;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Catalouge quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Catalouge createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Catalouge updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Catalouge createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Catalouge updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Categories getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categories categories) {
        this.categorie = categories;
    }

    public Catalouge categorie(Categories categories) {
        this.setCategorie(categories);
        return this;
    }

    public GstSlave getTaxSlaves() {
        return this.taxSlaves;
    }

    public void setTaxSlaves(GstSlave gstSlave) {
        this.taxSlaves = gstSlave;
    }

    public Catalouge taxSlaves(GstSlave gstSlave) {
        this.setTaxSlaves(gstSlave);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalouge)) {
            return false;
        }
        return catalougeId != null && catalougeId.equals(((Catalouge) o).catalougeId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalouge{" +
            "catalougeId=" + getCatalougeId() +
            ", name='" + getName() + "'" +
            ", cost=" + getCost() +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", hsnNo=" + getHsnNo() +
            ", quantity=" + getQuantity() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
