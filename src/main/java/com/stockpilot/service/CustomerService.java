package com.stockpilot.service;

import com.stockpilot.exception.InvalidInputException;
import com.stockpilot.model.Customer;
import com.stockpilot.repository.CustomerRepository;

import java.util.List;
import java.util.regex.Pattern;

public class CustomerService {
    private final CustomerRepository customerRepository;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\-]{9,15}$");

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(Customer customer) {
        validateEmail(customer.getEmail());
        validatePhone(customer.getPhone());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Customer with ID " + id + " not found."));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void updateCustomer(Customer customer) {
        if (customer.getId() == null) {
            throw new InvalidInputException("Customer ID cannot be null for update.");
        }
        validateEmail(customer.getEmail());
        validatePhone(customer.getPhone());
        getCustomerById(customer.getId());
        customerRepository.update(customer);
    }

    public void deleteCustomer(Long id) {
        getCustomerById(id);
        customerRepository.deleteById(id);
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("Invalid email format.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidInputException("Invalid phone number format.");
        }
    }
}
