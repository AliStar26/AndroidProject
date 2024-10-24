package com.example.androidproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.API.ProductApiService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProductFormActivity extends AppCompatActivity {
    private EditText nameEditText, priceEditText;
    private Button actionButton;
    private ProductApiService productApiService;
    private String action;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView productImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productApiService = new ProductApiService(this);
        nameEditText = findViewById(R.id.product_name);
        priceEditText = findViewById(R.id.product_price);
        actionButton = findViewById(R.id.action_button);
        productImage = findViewById(R.id.product_image);

        action = getIntent().getStringExtra("action");

        Button selectImageButton = findViewById(R.id.select_image_button);
        selectImageButton.setOnClickListener(view -> openFileChooser());
        productImage.setOnClickListener(v -> {
            if (imageUri != null) {
            }
        });

        if ("update".equals(action)) {
            int productId = getIntent().getIntExtra("productId", -1);
            String productName = getIntent().getStringExtra("productName");
            double productPrice = getIntent().getDoubleExtra("productPrice", 0.0);

            nameEditText.setText(productName);
            priceEditText.setText(String.valueOf(productPrice));
        }

        setupAction();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    private void setupAction() {
        switch (action) {
            case "add":
                actionButton.setText("Add Product");
                actionButton.setOnClickListener(v -> addProduct());
                break;
            case "update":
                actionButton.setText("Update Product");
                actionButton.setOnClickListener(v -> updateProduct());
                break;
            case "delete":
                actionButton.setText("Delete Product");
                actionButton.setOnClickListener(v -> {
                    Toast.makeText(ProductFormActivity.this, "Delete action is not implemented", Toast.LENGTH_SHORT).show();
                });
                nameEditText.setVisibility(View.GONE);
                priceEditText.setVisibility(View.GONE);
                break;
        }
    }

    private void addProduct() {
        String name = nameEditText.getText().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());
        String imagePath = saveImageLocally(imageUri);

        if (imagePath != null) {
            Picasso.get()
                    .load(imageUri)
                    .into(productImage);
        }

        productApiService.addProduct(name, price, imagePath, response -> {
            Toast.makeText(ProductFormActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }, error -> Toast.makeText(ProductFormActivity.this, "Error adding product", Toast.LENGTH_SHORT).show());
    }

    private void updateProduct() {
        String name = nameEditText.getText().toString();
        String priceStr = priceEditText.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int productId = getIntent().getIntExtra("productId", -1);
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = saveImageLocally(imageUri);

        if (imagePath != null) {
            Picasso.get()
                    .load(imageUri)
                    .into(productImage);
        }

        productApiService.updateProduct(productId, name, price, imagePath, response -> {
            Toast.makeText(ProductFormActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }, error -> Toast.makeText(ProductFormActivity.this, "Error updating product", Toast.LENGTH_SHORT).show());
    }

    private String saveImageLocally(Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(storageDir, "product_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e("Image Error", "Error saving image", e);
            return null;
        }
    }
}