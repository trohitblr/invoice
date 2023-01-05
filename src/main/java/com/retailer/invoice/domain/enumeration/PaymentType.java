package com.retailer.invoice.domain.enumeration;

/**
 * The PaymentType enumeration.
 */
public enum PaymentType {
    NEFT("neft"),
    UPI("upi"),
    CASH("cash"),
    CREDIT("credit"),
    DEBIT("debit");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
