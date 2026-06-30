package com.stockpilot.service;

import com.stockpilot.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class PercentageDiscount implements DiscountPolicy {
    private final BigDecimal percentage;

    public PercentageDiscount(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculateDiscount(List<OrderItem> items) {
        BigDecimal total = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.multiply(percentage);
    }
}
