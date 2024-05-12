package com.my.shop.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        priceTextView = findViewById(R.id.priceTextView);

        // Retrieve the product object passed from ProductListActivity
        Product product = (Product) getIntent().getSerializableExtra("product");
        displayProductDetails(product);

        findViewById(R.id.addToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OptionalInt indexOptional = IntStream.range(0, MyApp.cartList.size())
                        .filter(i -> {
                            assert product != null;
                            return Objects.equals(MyApp.cartList.get(i).getpId(), product.getpId());
                        })
                        .findFirst();

                // Check if a matching index is found
                if (indexOptional.isPresent()) {
                    int index = indexOptional.getAsInt();
                    int qty = MyApp.cartList.get(index).getQuantity();
                    MyApp.cartList.get(index).setQuantity(qty+1);
                } else {
                    CartItem cartItem = new CartItem(product.getpId(), product.getName(), product.getPrice(), 1);
                    MyApp.cartList.add(cartItem);
                }
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });

        findViewById(R.id.viewCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });
    }

    private void displayProductDetails(Product product) {
        if (product != null) {
            nameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            priceTextView.setText(String.valueOf(product.getPrice()));

            Glide.with(this).load(product.getImageUrl()).into(imageView);
        }
    }
}