package com.retailer.invoice.repository;

import com.retailer.invoice.domain.RetailerUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RetailerUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetailerUserRepository extends JpaRepository<RetailerUser, Long> {}
