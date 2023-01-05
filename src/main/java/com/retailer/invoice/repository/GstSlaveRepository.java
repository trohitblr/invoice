package com.retailer.invoice.repository;

import com.retailer.invoice.domain.GstSlave;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GstSlave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GstSlaveRepository extends JpaRepository<GstSlave, Long> {}
