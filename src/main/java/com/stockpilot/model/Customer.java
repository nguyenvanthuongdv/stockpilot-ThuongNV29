package com.stockpilot.model;

public class Customer {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public Customer(Long id, String name, String email, String phone) {
        if(name==null ||name.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if(email==null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
