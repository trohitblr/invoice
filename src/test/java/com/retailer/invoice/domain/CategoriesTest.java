package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categories.class);
        Categories categories1 = new Categories();
        categories1.setCategoryId(1L);
        Categories categories2 = new Categories();
        categories2.setCategoryId(categories1.getCategoryId());
        assertThat(categories1).isEqualTo(categories2);
        categories2.setCategoryId(2L);
        assertThat(categories1).isNotEqualTo(categories2);
        categories1.setCategoryId(null);
        assertThat(categories1).isNotEqualTo(categories2);
    }
}
