package com.example.androidproject.Models;

public class Client {
    private int id;
    private int keyNumber;
    private boolean isActive;

    public Client(int id, int keyNumber, boolean isActive) {
        this.id = id;
        this.keyNumber = keyNumber;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
