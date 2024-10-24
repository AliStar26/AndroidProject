package com.example.androidproject.Models;

public class ClientDto {
    private String name;
    private String phoneNumber;
    private int keyNumber;
    private int id;

    public ClientDto(String name, String phoneNumber, int keyNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.keyNumber = keyNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }
}
