package com.stockpilot.service;

import com.stockpilot.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class BulkDiscount implements DiscountPolicy {
    private final int bulkThreshold;
    private final BigDecimal discountPerItem;

    public BulkDiscount(int bulkThreshold, BigDecimal discountPerItem) {
        this.bulkThreshold = bulkThreshold;
        this.discountPerItem = discountPerItem;
    }

    @Override
    public BigDecimal calculateDiscount(List<OrderItem> items) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (OrderItem item : items) {
            if (item.getQuantity() >= bulkThreshold) {
                totalDiscount = totalDiscount.add(discountPerItem.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return totalDiscount;
    }
}
