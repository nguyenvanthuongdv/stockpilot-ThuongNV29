package com.stockpilot.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private List<OrderItem> orderItems;

    public Order(Long id, Long customerId, LocalDateTime orderDate, BigDecimal totalAmount, List<OrderItem> orderItems) {
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Total amount must be greater than 0");
        }
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
