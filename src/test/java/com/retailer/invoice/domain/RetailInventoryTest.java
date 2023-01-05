package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetailInventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetailInventory.class);
        RetailInventory retailInventory1 = new RetailInventory();
        retailInventory1.setRetailInventoryId(1L);
        RetailInventory retailInventory2 = new RetailInventory();
        retailInventory2.setRetailInventoryId(retailInventory1.getRetailInventoryId());
        assertThat(retailInventory1).isEqualTo(retailInventory2);
        retailInventory2.setRetailInventoryId(2L);
        assertThat(retailInventory1).isNotEqualTo(retailInventory2);
        retailInventory1.setRetailInventoryId(null);
        assertThat(retailInventory1).isNotEqualTo(retailInventory2);
    }
}
