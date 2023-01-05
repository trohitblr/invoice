package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceLineItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLineItem.class);
        InvoiceLineItem invoiceLineItem1 = new InvoiceLineItem();
        invoiceLineItem1.setInvoiceLineItemId(1L);
        InvoiceLineItem invoiceLineItem2 = new InvoiceLineItem();
        invoiceLineItem2.setInvoiceLineItemId(invoiceLineItem1.getInvoiceLineItemId());
        assertThat(invoiceLineItem1).isEqualTo(invoiceLineItem2);
        invoiceLineItem2.setInvoiceLineItemId(2L);
        assertThat(invoiceLineItem1).isNotEqualTo(invoiceLineItem2);
        invoiceLineItem1.setInvoiceLineItemId(null);
        assertThat(invoiceLineItem1).isNotEqualTo(invoiceLineItem2);
    }
}
