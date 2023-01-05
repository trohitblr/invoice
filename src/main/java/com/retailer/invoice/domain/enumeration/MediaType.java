package com.retailer.invoice.domain.enumeration;

/**
 * The MediaType enumeration.
 */
public enum MediaType {
    PNG("png"),
    JEPG("jpeg");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
