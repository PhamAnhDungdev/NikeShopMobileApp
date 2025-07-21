package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;
import com.example.nikeshop.adapters.CategoryAdapter;

public class HomeActivity extends BottomMenuActivity {
    private CategoryAdapter categoryAdapter;
    private com.example.nikeshop.ui.ViewModels.CategoryViewModel categoryViewModel;

    private com.example.nikeshop.ui.ViewModels.ProductViewModel productViewModel;
    private com.example.nikeshop.ui.adapters.ProductAdapter productAdapter;

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
        setupBottomMenu(R.id.nav_home);

        // Setup RecyclerView cho danh sách category
        androidx.recyclerview.widget.RecyclerView rvCategories = findViewById(R.id.rv_categories);
        categoryAdapter = new CategoryAdapter(this, null, category -> {
            Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getName());
            startActivity(intent);
        });
        rvCategories.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Lấy dữ liệu từ ViewModel
        categoryViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.setCategories(categories);
        });

        // Setup RecyclerView cho danh sách sản phẩm
        androidx.recyclerview.widget.RecyclerView rvProducts = findViewById(R.id.rv_products);
        productAdapter = new com.example.nikeshop.ui.adapters.ProductAdapter(this, null, product -> {
            openProductDetail(product.getId());
        });
        rvProducts.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Lấy dữ liệu từ ViewModel
        productViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            productAdapter.setProducts(products);
        });
        // Sự kiện click cho icon Cart
        ImageView btnCart = findViewById(R.id.btn_cart);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }
    }

    private void openProductDetail(int productId) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }
}