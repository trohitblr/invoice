package com.retailer.invoice.domain;

import com.retailer.invoice.domain.enumeration.MediaType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long mediaId;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getMediaId() {
        return this.mediaId;
    }

    public Media mediaId(Long mediaId) {
        this.setMediaId(mediaId);
        return this;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getPath() {
        return this.path;
    }

    public Media path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MediaType getType() {
        return this.type;
    }

    public Media type(MediaType type) {
        this.setType(type);
        return this;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return mediaId != null && mediaId.equals(((Media) o).mediaId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Media{" +
            "mediaId=" + getMediaId() +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
