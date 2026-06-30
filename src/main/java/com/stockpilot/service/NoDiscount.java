package com.stockpilot.service;

import com.stockpilot.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class NoDiscount implements DiscountPolicy {
    @Override
    public BigDecimal calculateDiscount(List<OrderItem> items) {
        return BigDecimal.ZERO;
    }
}
