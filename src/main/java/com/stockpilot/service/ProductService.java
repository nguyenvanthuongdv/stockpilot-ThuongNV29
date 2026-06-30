package com.stockpilot.service;

import com.stockpilot.exception.InvalidInputException;
import com.stockpilot.exception.ProductNotFoundException;
import com.stockpilot.model.Product;
import com.stockpilot.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ProductService {
    private final ProductRepository productRepository;
    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]{3}-\\d{4}$");

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        validateSku(product.getSku());
        Optional<Product> existing = productRepository.findBySku(product.getSku());
        if (existing.isPresent()) {
            throw new InvalidInputException("Product with SKU " + product.getSku() + " already exists.");
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found."));
    }

    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product with SKU " + sku + " not found."));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void updateProduct(Product product) {
        if (product.getId() == null) {
            throw new InvalidInputException("Product ID cannot be null for update.");
        }
        validateSku(product.getSku());
        getProductById(product.getId());
        productRepository.update(product);
    }

    public void deleteProduct(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }

    public void adjustStock(Long id, int quantityChange) {
        Product product = getProductById(id);
        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new InvalidInputException("Stock cannot be negative. Current stock: " + product.getQuantity());
        }
        product.setQuantity(newQuantity);
        productRepository.update(product);
    }

    private void validateSku(String sku) {
        if (sku == null || !SKU_PATTERN.matcher(sku).matches()) {
            throw new InvalidInputException("Invalid SKU format. Must match ^[A-Z]{3}-\\d{4}$ (e.g., APP-0001)");
        }
    }
}
