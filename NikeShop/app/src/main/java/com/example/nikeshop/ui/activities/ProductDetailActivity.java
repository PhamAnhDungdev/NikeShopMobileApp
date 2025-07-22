package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;

public class ProductDetailActivity extends AppCompatActivity {
    private com.example.nikeshop.data.local.entity.Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Lấy product_id từ Intent
        int productId = getIntent().getIntExtra("product_id", -1);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind dữ liệu sản phẩm ra giao diện
        com.example.nikeshop.ui.ViewModels.ProductViewModel productViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            if (products != null) {
                for (com.example.nikeshop.data.local.entity.Product product : products) {
                    if (product.getId() == productId) {
                        currentProduct = product;
                        // Ảnh sản phẩm
                        ImageView imgProduct = findViewById(R.id.product_image);
                        if (imgProduct != null && product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                            com.bumptech.glide.Glide.with(this).load(product.getImageUrl()).into(imgProduct);
                        }
                        // Tên sản phẩm
                        android.widget.TextView tvName = findViewById(R.id.tv_product_name);
                        if (tvName != null) tvName.setText(product.getName());
                        // Mô tả
                        android.widget.TextView tvDesc = findViewById(R.id.tv_product_desc);
                        if (tvDesc != null) tvDesc.setText(product.getDescription());
                        // Giá
                        android.widget.TextView tvPrice = findViewById(R.id.tv_product_price);
                        if (tvPrice != null) tvPrice.setText("$" + String.format("%.2f", product.getPrice()));
                        // Size (hiển thị size đầu tiên nếu có)
                        android.widget.TextView tvSize = findViewById(R.id.tv_product_size);
                        if (tvSize != null && product.getSize() != null) tvSize.setText(product.getSize());
                        // Số lượng người dùng chọn (mặc định là 1)
                        android.widget.TextView tvQuantity = findViewById(R.id.tv_quantity);
                        if (tvQuantity != null) tvQuantity.setText("1");
                        // Stock (số lượng sản phẩm hiện có trong db)
                        android.widget.TextView tvStock = findViewById(R.id.tv_product_stock);
                        if (tvStock != null) tvStock.setText("Stock: " + product.getStockQuantity());
                        android.widget.TextView btnMinus = findViewById(R.id.btn_minus);
                        android.widget.TextView btnPlus = findViewById(R.id.btn_plus);
                        final int[] selectedQuantity = {1};
                        btnMinus.setOnClickListener(v -> {
                            if (selectedQuantity[0] > 1) {
                                selectedQuantity[0]--;
                                tvQuantity.setText(String.valueOf(selectedQuantity[0]));
                            }
                        });
                        btnPlus.setOnClickListener(v -> {
                            selectedQuantity[0]++;
                            tvQuantity.setText(String.valueOf(selectedQuantity[0]));
                        });
                        break;
                    }
                }
            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        ImageView btnCart = findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
                if (!isLoggedIn) {
                    android.widget.Toast.makeText(ProductDetailActivity.this, "Bạn cần đăng nhập để xem giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                       finish(); // Đảm bảo không chạy tiếp code thêm vào giỏ hàng
                    return;
                } else {
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
        // View Comments button
        android.widget.TextView btnViewComments = findViewById(R.id.btn_view_comments);
        btnViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct != null) {
                    android.util.Log.d("ProductDetailActivity", "View Comments clicked, productId: " + currentProduct.getId());
                    Intent intent = new Intent(ProductDetailActivity.this, FeedbackActivity.class);
                    intent.putExtra("product_id", currentProduct.getId()); // Lấy id thực tế từ DB
                    startActivity(intent);
                }
            }
        });
        // Xử lý nút Add to Cart (theo XML là TextView)
        android.widget.TextView btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
                if (!isLoggedIn) {
                    android.widget.Toast.makeText(ProductDetailActivity.this, "Bạn cần đăng nhập để mua hàng!", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (currentProduct != null) {
                    android.widget.TextView tvQuantity = findViewById(R.id.tv_quantity);
                    int quantity = 1;
                    try {
                        quantity = Integer.parseInt(tvQuantity.getText().toString());
                    } catch (Exception e) {}
                    // Thêm vào giỏ hàng qua ViewModel (giả sử đã có CartViewModel)
                    com.example.nikeshop.ui.ViewModels.CartViewModel cartViewModel = new androidx.lifecycle.ViewModelProvider(ProductDetailActivity.this).get(com.example.nikeshop.ui.ViewModels.CartViewModel.class);
                    cartViewModel.addToCart(currentProduct.getId(), quantity);
                    android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút Favourites (theo XML là TextView)
        android.widget.TextView btnFavourite = findViewById(R.id.btn_favourite);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
                if (!isLoggedIn) {
                    android.widget.Toast.makeText(ProductDetailActivity.this, "Bạn cần đăng nhập để thêm vào yêu thích!", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                if (currentProduct != null) {
                    com.example.nikeshop.ui.ViewModels.WishlistViewModel wishlistViewModel = new androidx.lifecycle.ViewModelProvider(ProductDetailActivity.this).get(com.example.nikeshop.ui.ViewModels.WishlistViewModel.class);
                    wishlistViewModel.addToWishlist(currentProduct.getId(), new com.example.nikeshop.ui.ViewModels.WishlistViewModel.AddToWishlistListener() {
                        @Override
                        public void onSuccess() {
                            android.widget.Toast.makeText(ProductDetailActivity.this, "Đã thêm sản phẩm vào danh sách yêu thích!", android.widget.Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onExists() {
                            android.widget.Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã có trong danh sách yêu thích!", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}