package com.retailer.invoice.repository;

import com.retailer.invoice.domain.RetailInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RetailInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetailInventoryRepository extends JpaRepository<RetailInventory, Long> {}
