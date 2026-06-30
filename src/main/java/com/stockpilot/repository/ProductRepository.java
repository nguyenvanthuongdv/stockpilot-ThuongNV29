package com.stockpilot.repository;

import com.stockpilot.exception.DataAccessException;
import com.stockpilot.model.Product;
import com.stockpilot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements Repository<Product, Long> {

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (sku, name, category, price, stock_quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getQuantity());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
            return product;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save product", e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find product by id", e);
        }
        return Optional.empty();
    }
    
    public Optional<Product> findBySku(String sku) {
        String sql = "SELECT * FROM products WHERE sku = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find product by sku", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all products", e);
        }
        return products;
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET sku = ?, name = ?, category = ?, price = ?, stock_quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getQuantity());
            stmt.setLong(6, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update product", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete product", e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getLong("id"),
            rs.getString("sku"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getBigDecimal("price"),
            rs.getInt("stock_quantity")
        );
    }
}
