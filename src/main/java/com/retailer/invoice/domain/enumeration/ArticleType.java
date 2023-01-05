package com.retailer.invoice.domain.enumeration;

/**
 * The ArticleType enumeration.
 */
public enum ArticleType {
    GNG("gng"),
    AIR("air");

    private final String value;

    ArticleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
