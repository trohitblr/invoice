package com.retailer.invoice.repository;

import com.retailer.invoice.domain.Retailer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class RetailerRepositoryWithBagRelationshipsImpl implements RetailerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Retailer> fetchBagRelationships(Optional<Retailer> retailer) {
        return retailer.map(this::fetchCustomers);
    }

    @Override
    public Page<Retailer> fetchBagRelationships(Page<Retailer> retailers) {
        return new PageImpl<>(fetchBagRelationships(retailers.getContent()), retailers.getPageable(), retailers.getTotalElements());
    }

    @Override
    public List<Retailer> fetchBagRelationships(List<Retailer> retailers) {
        return Optional.of(retailers).map(this::fetchCustomers).orElse(Collections.emptyList());
    }

    Retailer fetchCustomers(Retailer result) {
        return entityManager
            .createQuery(
                "select retailer from Retailer retailer left join fetch retailer.customers where retailer is :retailer",
                Retailer.class
            )
            .setParameter("retailer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Retailer> fetchCustomers(List<Retailer> retailers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, retailers.size()).forEach(index -> order.put(retailers.get(index).getRetailerId(), index));
        List<Retailer> result = entityManager
            .createQuery(
                "select distinct retailer from Retailer retailer left join fetch retailer.customers where retailer in :retailers",
                Retailer.class
            )
            .setParameter("retailers", retailers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getRetailerId()), order.get(o2.getRetailerId())));
        return result;
    }
}
