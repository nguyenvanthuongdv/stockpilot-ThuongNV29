package com.stockpilot.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String sku;
    private String name;
    private String category;
    private BigDecimal price;
    private int stockQuantity;

    public Product(Long id, String sku, String name, String category, BigDecimal price, int stockQuantity) {
        if(price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if(stockQuantity < 0){
            throw new IllegalArgumentException("Stock quantity must be greater than 0");
        }
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return stockQuantity;
    }

    public void setQuantity(int quantity) {
        this.stockQuantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stockQuantity == product.stockQuantity && Objects.equals(id, product.id) && Objects.equals(sku, product.sku) && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, category, price, stockQuantity);
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + stockQuantity +
                '}';
    }
}
