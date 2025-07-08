package com.example.nikeshop.ui.activities;

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

import com.example.nikeshop.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView btnViewProductList = findViewById(R.id.btn_view_product_list);
        btnViewProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        // Product Card Clicks
        ImageView shoeImg1 = findViewById(R.id.shoe_img1);

        View.OnClickListener detailListener = v -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            startActivity(intent);
        };
        if (shoeImg1 != null) shoeImg1.setOnClickListener(detailListener);

        ImageView shoeImg2 = findViewById(R.id.shoe_img2);

        if (shoeImg2 != null) shoeImg2.setOnClickListener(detailListener);


        ImageView shoeImg3 = findViewById(R.id.shoe_img3);

        if (shoeImg3 != null) shoeImg3.setOnClickListener(detailListener);


        ImageView shoeImg4 = findViewById(R.id.shoe_img4);

        if (shoeImg4 != null) shoeImg4.setOnClickListener(detailListener);

        ImageView btnCart = findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }
}