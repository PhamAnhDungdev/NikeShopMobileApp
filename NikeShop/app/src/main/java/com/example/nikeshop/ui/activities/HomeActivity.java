package com.example.nikeshop.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
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
        // Truyền CartViewModel và WishlistViewModel vào ProductAdapter
        com.example.nikeshop.ui.ViewModels.CartViewModel cartViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.CartViewModel.class);
        com.example.nikeshop.ui.ViewModels.WishlistViewModel wishlistViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.WishlistViewModel.class);
        productAdapter.setCartViewModel(cartViewModel);
        productAdapter.setWishlistViewModel(wishlistViewModel);
        rvProducts.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Lấy dữ liệu từ ViewModel
        productViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            productAdapter.setProducts(products);
        });
        // Sự kiện click cho icon Cart
        ImageView btnCart = findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
                if (!isLoggedIn) {
                    android.widget.Toast.makeText(HomeActivity.this, "Bạn cần đăng nhập để xem giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Đảm bảo không chạy tiếp code thêm vào giỏ hàng
                    return;
                } else {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void openProductDetail(int productId) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }
}