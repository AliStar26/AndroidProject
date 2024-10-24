package com.example.androidproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Models.OrderItem;
import com.example.androidproject.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<OrderItem> cartItems;
    private OnQuantityChangeListener onQuantityChangeListener;

    public CartAdapter(List<OrderItem> cartItems, OnQuantityChangeListener onQuantityChangeListener) {
        this.cartItems = cartItems;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem orderItem = cartItems.get(position);
        holder.productName.setText(orderItem.getName());
        holder.productPrice.setText(String.valueOf(orderItem.getPrice()));
        holder.productQuantity.setText(String.valueOf(orderItem.getCount()));

        holder.increaseQuantityButton.setOnClickListener(v -> {
            orderItem.setCount(orderItem.getCount() + 1);
            notifyItemChanged(position);
            onQuantityChangeListener.onQuantityChanged();
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (orderItem.getCount() > 1) {
                orderItem.setCount(orderItem.getCount() - 1);
                notifyItemChanged(position);
                onQuantityChangeListener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        Button increaseQuantityButton, decreaseQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }
}