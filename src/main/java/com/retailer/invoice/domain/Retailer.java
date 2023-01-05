package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Retailer.
 */
@Entity
@Table(name = "retailer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Retailer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retailer_id")
    private Long retailerId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner")
    private String owner;

    @Min(value = 10)
    @Max(value = 10)
    @Column(name = "phone")
    private Integer phone;

    @Pattern(regexp = "^(.+)@(\\\\S+)$")
    @Column(name = "email")
    private String email;

    @Column(name = "gst_number")
    private String gstNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

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

    @OneToOne
    @JoinColumn(unique = true)
    private Media logo;

    @OneToMany(mappedBy = "retailers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoiceLineItems", "invoiceGStLineItems", "retailers" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(mappedBy = "retailers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "retailers" }, allowSetters = true)
    private Set<RetailerUser> retailerUsers = new HashSet<>();

    @OneToMany(mappedBy = "catalougs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "catalougs" }, allowSetters = true)
    private Set<RetailInventory> retailInventories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_retailer__customers",
        joinColumns = @JoinColumn(name = "retailer_retailer_id"),
        inverseJoinColumns = @JoinColumn(name = "customers_customer_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoiceAddress", "billingAddress", "retailers" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getRetailerId() {
        return this.retailerId;
    }

    public Retailer retailerId(Long retailerId) {
        this.setRetailerId(retailerId);
        return this;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public String getName() {
        return this.name;
    }

    public Retailer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return this.owner;
    }

    public Retailer owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public Retailer phone(Integer phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Retailer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstNumber() {
        return this.gstNumber;
    }

    public Retailer gstNumber(String gstNumber) {
        this.setGstNumber(gstNumber);
        return this;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public Status getStatus() {
        return this.status;
    }

    public Retailer status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Retailer createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public Retailer updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Retailer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Retailer updatedBy(String updatedBy) {
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

    public Retailer invoiceAddress(Address address) {
        this.setInvoiceAddress(address);
        return this;
    }

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address address) {
        this.billingAddress = address;
    }

    public Retailer billingAddress(Address address) {
        this.setBillingAddress(address);
        return this;
    }

    public Media getLogo() {
        return this.logo;
    }

    public void setLogo(Media media) {
        this.logo = media;
    }

    public Retailer logo(Media media) {
        this.setLogo(media);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setRetailers(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setRetailers(this));
        }
        this.invoices = invoices;
    }

    public Retailer invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Retailer addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setRetailers(this);
        return this;
    }

    public Retailer removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setRetailers(null);
        return this;
    }

    public Set<RetailerUser> getRetailerUsers() {
        return this.retailerUsers;
    }

    public void setRetailerUsers(Set<RetailerUser> retailerUsers) {
        if (this.retailerUsers != null) {
            this.retailerUsers.forEach(i -> i.setRetailers(null));
        }
        if (retailerUsers != null) {
            retailerUsers.forEach(i -> i.setRetailers(this));
        }
        this.retailerUsers = retailerUsers;
    }

    public Retailer retailerUsers(Set<RetailerUser> retailerUsers) {
        this.setRetailerUsers(retailerUsers);
        return this;
    }

    public Retailer addRetailerUser(RetailerUser retailerUser) {
        this.retailerUsers.add(retailerUser);
        retailerUser.setRetailers(this);
        return this;
    }

    public Retailer removeRetailerUser(RetailerUser retailerUser) {
        this.retailerUsers.remove(retailerUser);
        retailerUser.setRetailers(null);
        return this;
    }

    public Set<RetailInventory> getRetailInventories() {
        return this.retailInventories;
    }

    public void setRetailInventories(Set<RetailInventory> retailInventories) {
        if (this.retailInventories != null) {
            this.retailInventories.forEach(i -> i.setCatalougs(null));
        }
        if (retailInventories != null) {
            retailInventories.forEach(i -> i.setCatalougs(this));
        }
        this.retailInventories = retailInventories;
    }

    public Retailer retailInventories(Set<RetailInventory> retailInventories) {
        this.setRetailInventories(retailInventories);
        return this;
    }

    public Retailer addRetailInventory(RetailInventory retailInventory) {
        this.retailInventories.add(retailInventory);
        retailInventory.setCatalougs(this);
        return this;
    }

    public Retailer removeRetailInventory(RetailInventory retailInventory) {
        this.retailInventories.remove(retailInventory);
        retailInventory.setCatalougs(null);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Retailer customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Retailer addCustomers(Customer customer) {
        this.customers.add(customer);
        customer.getRetailers().add(this);
        return this;
    }

    public Retailer removeCustomers(Customer customer) {
        this.customers.remove(customer);
        customer.getRetailers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Retailer)) {
            return false;
        }
        return retailerId != null && retailerId.equals(((Retailer) o).retailerId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Retailer{" +
            "retailerId=" + getRetailerId() +
            ", name='" + getName() + "'" +
            ", owner='" + getOwner() + "'" +
            ", phone=" + getPhone() +
            ", email='" + getEmail() + "'" +
            ", gstNumber='" + getGstNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
