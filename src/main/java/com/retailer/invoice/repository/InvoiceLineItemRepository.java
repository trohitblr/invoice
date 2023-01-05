package com.retailer.invoice.repository;

import com.retailer.invoice.domain.InvoiceLineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, Long> {}
