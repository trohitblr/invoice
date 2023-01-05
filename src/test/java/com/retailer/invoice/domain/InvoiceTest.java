package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(invoice1.getInvoiceId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setInvoiceId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setInvoiceId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }
}
