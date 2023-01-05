package com.retailer.invoice.repository;

import com.retailer.invoice.domain.InvoiceGStLineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceGStLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceGStLineItemRepository extends JpaRepository<InvoiceGStLineItem, Long> {}
