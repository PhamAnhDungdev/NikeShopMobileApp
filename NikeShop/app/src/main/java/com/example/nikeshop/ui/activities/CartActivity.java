package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;
import com.example.nikeshop.adapters.CartAdapter;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        TextView btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        // Lấy user_id từ SharedPreferences
        android.content.SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId == -1) {
            android.widget.Toast.makeText(this, "Bạn cần đăng nhập để xem giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Hiển thị danh sách sản phẩm giỏ hàng lên RecyclerView
        androidx.recyclerview.widget.RecyclerView recyclerView = findViewById(R.id.recycler_cart_items);
        CartAdapter cartAdapter = new CartAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        com.example.nikeshop.ui.ViewModels.CartViewModel cartViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.nikeshop.ui.ViewModels.CartViewModel.class);
        cartAdapter.setOnQuantityChangeListener((position, newQuantity, cart) -> {
            cartViewModel.updateCartQuantity(cart.cartId, newQuantity);
        });
        cartAdapter.setOnDeleteClickListener((position, cart) -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    cartViewModel.deleteCart(cart.cartId);
                })
                .setNegativeButton("Huỷ", null)
                .show();
        });
        cartViewModel.getCartWithProductByUser(userId).observe(this, carts -> {
            cartAdapter.setCartList(carts);
            // Tính tổng giá trị giỏ hàng
            double total = 0;
            android.widget.LinearLayout layoutPriceDetails = findViewById(R.id.layout_price_details);
            layoutPriceDetails.removeAllViews();
            for (com.example.nikeshop.data.local.modelDto.CartWithProduct cart : carts) {
                total += cart.totalPrice;
                // Thêm dòng tên sản phẩm + tổng tiền
                android.widget.LinearLayout row = new android.widget.LinearLayout(this);
                row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
                android.widget.TextView tvName = new android.widget.TextView(this);
                tvName.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                tvName.setText(cart.productName);
                tvName.setTextSize(18);
                tvName.setPadding(0, 0, 8, 0);
                android.widget.TextView tvPrice = new android.widget.TextView(this);
                tvPrice.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
                tvPrice.setText("$" + String.format("%.2f", cart.totalPrice));
                tvPrice.setTextSize(18);
                tvPrice.setPadding(8, 0, 0, 0);
                row.addView(tvName);
                row.addView(tvPrice);
                layoutPriceDetails.addView(row);
            }
            // Hiển thị tổng lên TextView
            TextView tvTotal = findViewById(R.id.tv_cart_total);
            if (tvTotal != null) {
                tvTotal.setText("$" + String.format("%.2f", total));
            }
        });

        // Lấy địa chỉ user từ DB và hiển thị lên shipping address
        new Thread(() -> {
            com.example.nikeshop.data.local.AppDatabase db = com.example.nikeshop.data.local.AppDatabase.getInstance(getApplicationContext());
            com.example.nikeshop.data.local.entity.User user = db.userDao().getUserById(userId);
            String address = (user != null && user.getAddress() != null && !user.getAddress().isEmpty()) ? user.getAddress() : "No address";
            runOnUiThread(() -> {
                TextView tvAddress = findViewById(R.id.tv_shipping_address);
                if (tvAddress != null) {
                    tvAddress.setText(address);
                }
            });
        }).start();
    }
}