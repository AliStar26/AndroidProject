package com.example.androidproject.Models;

import java.util.List;

public class EndVisitResponse {
    private String message;
    private double totalAmount;
    private List<Order> orders;

    public String getMessage() {
        return message;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
