package com.stockpilot.service;

import com.stockpilot.exception.InvalidInputException;
import com.stockpilot.exception.ProductNotFoundException;
import com.stockpilot.model.Customer;
import com.stockpilot.model.Order;
import com.stockpilot.model.OrderItem;
import com.stockpilot.model.Product;
import com.stockpilot.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, ProductService productService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    public Order placeOrder(Long customerId, Map<String, Integer> cart, DiscountPolicy discountPolicy) {
        if (cart == null || cart.isEmpty()) {
            throw new InvalidInputException("Cart is empty.");
        }

        Customer customer = customerService.getCustomerById(customerId);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal subTotal = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String sku = entry.getKey();
            int qty = entry.getValue();

            Product product = productService.getProductBySku(sku);
            
            OrderItem item = new OrderItem(null, null, product.getId(), qty, product.getPrice());
            items.add(item);
            
            subTotal = subTotal.add(product.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        BigDecimal discount = discountPolicy.calculateDiscount(items);
        BigDecimal totalAmount = subTotal.subtract(discount);
        
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        Order order = new Order(null, customer.getId(), LocalDateTime.now(), totalAmount, items);
        
        return orderRepository.placeOrderAtomically(order);
    }
}
