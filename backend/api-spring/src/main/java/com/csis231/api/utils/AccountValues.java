package com.csis231.api.utils;

import java.math.BigDecimal;

public class AccountValues {

    private BigDecimal totalValue;
    private BigDecimal spotValue;
    private BigDecimal fiatValue;

    public AccountValues(BigDecimal totalValue, BigDecimal spotValue, BigDecimal fiatValue) {
        this.totalValue = totalValue;
        this.spotValue = spotValue;
        this.fiatValue = fiatValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public BigDecimal getSpotValue() {
        return spotValue;
    }

    public BigDecimal getFiatValue() {
        return fiatValue;
    }
}

