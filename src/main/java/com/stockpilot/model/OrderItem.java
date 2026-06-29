package com.stockpilot.model;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem(Long id, Long orderId, Long productId, int quantity, BigDecimal unitPrice) {
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if(unitPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
