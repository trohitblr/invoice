package com.retailer.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.retailer.invoice.domain.enumeration.UserType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RetailerUser.
 */
@Entity
@Table(name = "retailer_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RetailerUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retailer_user_id")
    private Long retailerUserId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @Column(name = "enc_password")
    private String encPassword;

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
    private Retailer retailers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getRetailerUserId() {
        return this.retailerUserId;
    }

    public RetailerUser retailerUserId(Long retailerUserId) {
        this.setRetailerUserId(retailerUserId);
        return this;
    }

    public void setRetailerUserId(Long retailerUserId) {
        this.retailerUserId = retailerUserId;
    }

    public String getUserId() {
        return this.userId;
    }

    public RetailerUser userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getPhone() {
        return this.phone;
    }

    public RetailerUser phone(Long phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public RetailerUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getType() {
        return this.type;
    }

    public RetailerUser type(UserType type) {
        this.setType(type);
        return this;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getEncPassword() {
        return this.encPassword;
    }

    public RetailerUser encPassword(String encPassword) {
        this.setEncPassword(encPassword);
        return this;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public RetailerUser status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public RetailerUser createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public RetailerUser updatedOn(Instant updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public RetailerUser createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public RetailerUser updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Retailer getRetailers() {
        return this.retailers;
    }

    public void setRetailers(Retailer retailer) {
        this.retailers = retailer;
    }

    public RetailerUser retailers(Retailer retailer) {
        this.setRetailers(retailer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetailerUser)) {
            return false;
        }
        return retailerUserId != null && retailerUserId.equals(((RetailerUser) o).retailerUserId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetailerUser{" +
            "retailerUserId=" + getRetailerUserId() +
            ", userId='" + getUserId() + "'" +
            ", phone=" + getPhone() +
            ", email='" + getEmail() + "'" +
            ", type='" + getType() + "'" +
            ", encPassword='" + getEncPassword() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
