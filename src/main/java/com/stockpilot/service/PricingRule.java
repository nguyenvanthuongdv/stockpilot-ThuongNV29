package com.stockpilot.service;

import java.math.BigDecimal;

@FunctionalInterface
public interface PricingRule {
    BigDecimal apply(BigDecimal originalPrice, int quantity);
}
