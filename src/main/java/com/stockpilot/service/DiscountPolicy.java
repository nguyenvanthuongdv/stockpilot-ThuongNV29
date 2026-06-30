package com.stockpilot.service;

import com.stockpilot.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountPolicy {
    BigDecimal calculateDiscount(List<OrderItem> items);
}
