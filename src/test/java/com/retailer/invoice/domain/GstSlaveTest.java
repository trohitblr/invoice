package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GstSlaveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GstSlave.class);
        GstSlave gstSlave1 = new GstSlave();
        gstSlave1.setGstSlaveId(1L);
        GstSlave gstSlave2 = new GstSlave();
        gstSlave2.setGstSlaveId(gstSlave1.getGstSlaveId());
        assertThat(gstSlave1).isEqualTo(gstSlave2);
        gstSlave2.setGstSlaveId(2L);
        assertThat(gstSlave1).isNotEqualTo(gstSlave2);
        gstSlave1.setGstSlaveId(null);
        assertThat(gstSlave1).isNotEqualTo(gstSlave2);
    }
}
