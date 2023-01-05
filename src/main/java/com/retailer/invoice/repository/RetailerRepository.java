package com.retailer.invoice.repository;

import com.retailer.invoice.domain.Retailer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Retailer entity.
 *
 * When extending this class, extend RetailerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface RetailerRepository extends RetailerRepositoryWithBagRelationships, JpaRepository<Retailer, Long> {
    default Optional<Retailer> findOneWithEagerRelationships(Long retailerId) {
        return this.fetchBagRelationships(this.findById(retailerId));
    }

    default List<Retailer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Retailer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
