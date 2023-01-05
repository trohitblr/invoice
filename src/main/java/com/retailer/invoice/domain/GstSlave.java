package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.GstType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GstSlave.
 */
@Entity
@Table(name = "gst_slave")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GstSlave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gst_slave_id")
    private Long gstSlaveId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax")
    private GstType tax;

    @Column(name = "tax_percentage")
    private Float taxPercentage;

    @OneToMany(mappedBy = "taxSlaves")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categorie", "taxSlaves" }, allowSetters = true)
    private Set<Catalouge> catalouges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getGstSlaveId() {
        return this.gstSlaveId;
    }

    public GstSlave gstSlaveId(Long gstSlaveId) {
        this.setGstSlaveId(gstSlaveId);
        return this;
    }

    public void setGstSlaveId(Long gstSlaveId) {
        this.gstSlaveId = gstSlaveId;
    }

    public GstType getTax() {
        return this.tax;
    }

    public GstSlave tax(GstType tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(GstType tax) {
        this.tax = tax;
    }

    public Float getTaxPercentage() {
        return this.taxPercentage;
    }

    public GstSlave taxPercentage(Float taxPercentage) {
        this.setTaxPercentage(taxPercentage);
        return this;
    }

    public void setTaxPercentage(Float taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Set<Catalouge> getCatalouges() {
        return this.catalouges;
    }

    public void setCatalouges(Set<Catalouge> catalouges) {
        if (this.catalouges != null) {
            this.catalouges.forEach(i -> i.setTaxSlaves(null));
        }
        if (catalouges != null) {
            catalouges.forEach(i -> i.setTaxSlaves(this));
        }
        this.catalouges = catalouges;
    }

    public GstSlave catalouges(Set<Catalouge> catalouges) {
        this.setCatalouges(catalouges);
        return this;
    }

    public GstSlave addCatalouge(Catalouge catalouge) {
        this.catalouges.add(catalouge);
        catalouge.setTaxSlaves(this);
        return this;
    }

    public GstSlave removeCatalouge(Catalouge catalouge) {
        this.catalouges.remove(catalouge);
        catalouge.setTaxSlaves(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GstSlave)) {
            return false;
        }
        return gstSlaveId != null && gstSlaveId.equals(((GstSlave) o).gstSlaveId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GstSlave{" +
            "gstSlaveId=" + getGstSlaveId() +
            ", tax='" + getTax() + "'" +
            ", taxPercentage=" + getTaxPercentage() +
            "}";
    }
}
