package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceGStLineItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceGStLineItem.class);
        InvoiceGStLineItem invoiceGStLineItem1 = new InvoiceGStLineItem();
        invoiceGStLineItem1.setInvoiceGStLineItemId(1L);
        InvoiceGStLineItem invoiceGStLineItem2 = new InvoiceGStLineItem();
        invoiceGStLineItem2.setInvoiceGStLineItemId(invoiceGStLineItem1.getInvoiceGStLineItemId());
        assertThat(invoiceGStLineItem1).isEqualTo(invoiceGStLineItem2);
        invoiceGStLineItem2.setInvoiceGStLineItemId(2L);
        assertThat(invoiceGStLineItem1).isNotEqualTo(invoiceGStLineItem2);
        invoiceGStLineItem1.setInvoiceGStLineItemId(null);
        assertThat(invoiceGStLineItem1).isNotEqualTo(invoiceGStLineItem2);
    }
}
