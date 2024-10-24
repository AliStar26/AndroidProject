package com.example.androidproject.Models;

import java.util.List;

public class ClientOrdersResponse {
    private String message;
    private double totalAmount;
    private List<Order> orders;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order {
        private int id;
        private int clientId;
        private String productName;
        private int count;
        private double price;
        private boolean isPaid;
        private int chequeNumber;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public void setPaid(boolean paid) {
            isPaid = paid;
        }

        public int getChequeNumber() {
            return chequeNumber;
        }

        public void setChequeNumber(int chequeNumber) {
            this.chequeNumber = chequeNumber;
        }
    }
}