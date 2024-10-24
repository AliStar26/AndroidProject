package com.example.androidproject.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidproject.Models.Product;
import com.example.androidproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnProductUpdateListener updateListener;
    private OnProductDeleteListener deleteListener;

    public interface OnProductUpdateListener {
        void onUpdate(Product product);
    }

    public interface OnProductDeleteListener {
        void onDelete(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductUpdateListener updateListener, OnProductDeleteListener deleteListener) {
        this.productList = productList;
        this.updateListener = updateListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));


        if (product.getImagePath() != null) {
            Picasso.get()
                    .load(new File(product.getImagePath()))
                    .resize(100, 100)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.updateButton.setOnClickListener(v -> updateListener.onUpdate(product));
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        Button updateButton, deleteButton;
        ImageView productImage;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name_text_view);
            priceTextView = itemView.findViewById(R.id.product_price_text_view);
            updateButton = itemView.findViewById(R.id.update_product_button);
            deleteButton = itemView.findViewById(R.id.delete_product_button);
            productImage = itemView.findViewById(R.id.product_image);

        }

    }
}
