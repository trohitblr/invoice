package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^(.+)@(\\\\S+)$")
    @Column(name = "email")
    private String email;

    @NotNull
    @Min(value = 10)
    @Max(value = 10)
    @Column(name = "phone", nullable = false)
    private Integer phone;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToOne
    @JoinColumn(unique = true)
    private Address invoiceAddress;

    @OneToOne
    @JoinColumn(unique = true)
    private Address billingAddress;

    @ManyToMany(mappedBy = "customers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "invoiceAddress", "billingAddress", "logo", "invoices", "retailerUsers", "retailInventories", "customers" },
        allowSetters = true
    )
    private Set<Retailer> retailers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getCustomerId() {
        return this.customerId;
    }

    public Customer customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public Customer phone(Integer phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Customer createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Customer updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Customer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Customer updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Address getInvoiceAddress() {
        return this.invoiceAddress;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }

    public Customer invoiceAddress(Address address) {
        this.setInvoiceAddress(address);
        return this;
    }

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address address) {
        this.billingAddress = address;
    }

    public Customer billingAddress(Address address) {
        this.setBillingAddress(address);
        return this;
    }

    public Set<Retailer> getRetailers() {
        return this.retailers;
    }

    public void setRetailers(Set<Retailer> retailers) {
        if (this.retailers != null) {
            this.retailers.forEach(i -> i.removeCustomers(this));
        }
        if (retailers != null) {
            retailers.forEach(i -> i.addCustomers(this));
        }
        this.retailers = retailers;
    }

    public Customer retailers(Set<Retailer> retailers) {
        this.setRetailers(retailers);
        return this;
    }

    public Customer addRetailers(Retailer retailer) {
        this.retailers.add(retailer);
        retailer.getCustomers().add(this);
        return this;
    }

    public Customer removeRetailers(Retailer retailer) {
        this.retailers.remove(retailer);
        retailer.getCustomers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return customerId != null && customerId.equals(((Customer) o).customerId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "customerId=" + getCustomerId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone=" + getPhone() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
