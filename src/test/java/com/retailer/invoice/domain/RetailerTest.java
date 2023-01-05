package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetailerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retailer.class);
        Retailer retailer1 = new Retailer();
        retailer1.setRetailerId(1L);
        Retailer retailer2 = new Retailer();
        retailer2.setRetailerId(retailer1.getRetailerId());
        assertThat(retailer1).isEqualTo(retailer2);
        retailer2.setRetailerId(2L);
        assertThat(retailer1).isNotEqualTo(retailer2);
        retailer1.setRetailerId(null);
        assertThat(retailer1).isNotEqualTo(retailer2);
    }
}
