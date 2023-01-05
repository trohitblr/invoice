package com.retailer.invoice.repository;

import com.retailer.invoice.domain.Catalouge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Catalouge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalougeRepository extends JpaRepository<Catalouge, Long> {
    Optional<List<Catalouge>> findByNameStartsWith(String name);
}
