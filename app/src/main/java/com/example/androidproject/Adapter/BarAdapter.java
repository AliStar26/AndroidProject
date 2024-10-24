package com.example.androidproject.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Models.Product;
import com.example.androidproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarViewHolder> {
    private List<Product> productsList;
    private OnProductClickListener onProductClickListener;

    public BarAdapter(List<Product> productsList, OnProductClickListener onProductClickListener) {
        this.productsList = productsList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public BarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new BarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarViewHolder holder, int position) {
        Product product = productsList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        if (product.getImagePath() != null) {
            Picasso.get()
                    .load(new File(product.getImagePath()))
                    .resize(100, 100)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.addToCartButton.setOnClickListener(v -> onProductClickListener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class BarViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        Button addToCartButton;
        ImageView productImage;

        public BarViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            productImage = itemView.findViewById(R.id.product_image);

        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
}
