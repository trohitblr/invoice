package com.retailer.invoice.domain.enumeration;

/**
 * The UserType enumeration.
 */
public enum UserType {
    OWNER("owner"),
    ADMIN("admin"),
    BILLING("billing"),
    AGENT("agent");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
