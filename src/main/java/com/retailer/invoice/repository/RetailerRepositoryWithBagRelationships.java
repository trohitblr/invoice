package com.retailer.invoice.repository;

import com.retailer.invoice.domain.Retailer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RetailerRepositoryWithBagRelationships {
    Optional<Retailer> fetchBagRelationships(Optional<Retailer> retailer);

    List<Retailer> fetchBagRelationships(List<Retailer> retailers);

    Page<Retailer> fetchBagRelationships(Page<Retailer> retailers);
}
