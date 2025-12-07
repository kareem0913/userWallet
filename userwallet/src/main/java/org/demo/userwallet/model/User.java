package org.demo.userwallet.model;

import java.sql.Timestamp;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Double walletBalance;
    private Boolean status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User(){}

    public User(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public User(Long id,
                String name,
                String email,
                String password,
                String phone,
                String address,
                Double walletBalance,
                Boolean status,
                Timestamp createdAt,
                Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.walletBalance = walletBalance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(Long id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public Boolean getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
