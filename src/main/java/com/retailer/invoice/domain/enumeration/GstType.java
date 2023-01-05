package com.retailer.invoice.domain.enumeration;

/**
 * The GstType enumeration.
 */
public enum GstType {
    CGSTSGST("gst"),
    OTHER("other");

    private final String value;

    GstType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
