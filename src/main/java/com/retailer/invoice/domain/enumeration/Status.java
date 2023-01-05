package com.retailer.invoice.domain.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
    ACTIVE("Active"),
    INACTIVE("InActive");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
