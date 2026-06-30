package com.stockpilot.repository;

import com.stockpilot.exception.DataAccessException;
import com.stockpilot.exception.InsufficientStockException;
import com.stockpilot.model.Order;
import com.stockpilot.model.OrderItem;
import com.stockpilot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<Order, Long> {

    @Override
    public Order save(Order entity) {
        throw new UnsupportedOperationException("Use placeOrderAtomically for atomic transaction saving");
    }

    public Order placeOrderAtomically(Order order) {
        String insertOrderSql = "INSERT INTO orders (customer_id, order_date, total_amount) VALUES (?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String checkStockSql = "SELECT stock_quantity FROM products WHERE id = ?";
        String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, order.getCustomerId());
                stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                stmt.setBigDecimal(3, order.getTotalAmount());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                    } else {
                        throw new DataAccessException("Creating order failed, no ID obtained.");
                    }
                }
            }

            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(order.getId());

                try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
                    checkStmt.setLong(1, item.getProductId());
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentStock = rs.getInt("stock_quantity");
                            if (currentStock < item.getQuantity()) {
                                throw new InsufficientStockException("Not enough stock for product ID " + item.getProductId() + ". Available: " + currentStock + ", Required: " + item.getQuantity());
                            }
                        } else {
                            throw new DataAccessException("Product ID " + item.getProductId() + " not found during checkout.");
                        }
                    }
                }

                try (PreparedStatement updateStmt = conn.prepareStatement(updateStockSql)) {
                    updateStmt.setInt(1, item.getQuantity());
                    updateStmt.setLong(2, item.getProductId());
                    updateStmt.executeUpdate();
                }

                try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS)) {
                    itemStmt.setLong(1, item.getOrderId());
                    itemStmt.setLong(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getUnitPrice());
                    itemStmt.executeUpdate();
                    
                    try (ResultSet rs = itemStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            item.setId(rs.getLong(1));
                        }
                    }
                }
            }

            conn.commit();
            return order;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException("Rollback failed!", ex);
                }
            }
            throw new DataAccessException("Transaction failed, rolled back.", e);
        } catch (InsufficientStockException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException("Rollback failed!", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new DataAccessException("Failed to close connection", e);
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty(); 
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(
                    rs.getLong("id"),
                    rs.getLong("customer_id"),
                    rs.getTimestamp("order_date").toLocalDateTime(),
                    rs.getBigDecimal("total_amount"),
                    new ArrayList<>()
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all orders", e);
        }
        return orders;
    }

    @Override
    public void update(Order entity) {
        throw new UnsupportedOperationException("Updates to placed orders are not supported");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Deleting orders is not supported");
    }
}
