package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;

public class ProductListActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        String categoryName = getIntent().getStringExtra("category_name");
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(categoryName);

        // Nhận category_id từ Intent
        int categoryId = getIntent().getIntExtra("category_id", -1);

        // Setup RecyclerView cho danh sách sản phẩm
        androidx.recyclerview.widget.RecyclerView rvProducts = findViewById(R.id.rv_products);
        com.example.nikeshop.ui.adapters.ProductAdapter productAdapter = new com.example.nikeshop.ui.adapters.ProductAdapter(this, null, product -> {
            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        rvProducts.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Lấy dữ liệu từ ViewModel
        com.example.nikeshop.ui.ViewModels.ProductViewModel productViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.ProductViewModel.class);
        if (categoryId != -1) {
            productViewModel.getProductsByCategory(categoryId).observe(this, products -> {
                productAdapter.setProducts(products);
            });
        } else {
            productViewModel.getAllProducts().observe(this, products -> {
                productAdapter.setProducts(products);
            });
        }

        ImageView btnCart = findViewById(R.id.btn_cart);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        setupBottomMenu(R.id.nav_home);
    }
}