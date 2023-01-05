package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalougeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalouge.class);
        Catalouge catalouge1 = new Catalouge();
        catalouge1.setCatalougeId(1L);
        Catalouge catalouge2 = new Catalouge();
        catalouge2.setCatalougeId(catalouge1.getCatalougeId());
        assertThat(catalouge1).isEqualTo(catalouge2);
        catalouge2.setCatalougeId(2L);
        assertThat(catalouge1).isNotEqualTo(catalouge2);
        catalouge1.setCatalougeId(null);
        assertThat(catalouge1).isNotEqualTo(catalouge2);
    }
}
