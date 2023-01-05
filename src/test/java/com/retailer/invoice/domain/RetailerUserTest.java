package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetailerUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetailerUser.class);
        RetailerUser retailerUser1 = new RetailerUser();
        retailerUser1.setRetailerUserId(1L);
        RetailerUser retailerUser2 = new RetailerUser();
        retailerUser2.setRetailerUserId(retailerUser1.getRetailerUserId());
        assertThat(retailerUser1).isEqualTo(retailerUser2);
        retailerUser2.setRetailerUserId(2L);
        assertThat(retailerUser1).isNotEqualTo(retailerUser2);
        retailerUser1.setRetailerUserId(null);
        assertThat(retailerUser1).isNotEqualTo(retailerUser2);
    }
}
