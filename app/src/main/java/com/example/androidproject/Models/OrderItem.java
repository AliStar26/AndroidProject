package com.example.androidproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {
    private String productName;
    private int count;
    private double price;

    public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.count = quantity;
        this.price = price;
    }

    protected OrderItem(Parcel in) {
        productName = in.readString();
        count = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }
    public String getProductName() {
        return productName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeInt(count);
        dest.writeDouble(price);
    }
}
