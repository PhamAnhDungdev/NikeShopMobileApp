package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.modelDto.ProductOrderDto;
import com.example.nikeshop.ui.ViewModels.OrderViewModel;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private ArrayList<ProductOrderDto> cartItems;
    private double totalAmount = 0.0;
    private String selectedPaymentMethod = "Cash On Delivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhan du lieu tu cart activity
        Intent intent = getIntent();
        cartItems = intent.getParcelableArrayListExtra("cart_items");
        totalAmount = intent.getDoubleExtra("total_amount", 0.0);

        // Hiển thị danh sách sản phẩm + tổng tiền
        LinearLayout layoutProductItems = findViewById(R.id.layout_product_items);
        layoutProductItems.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        // Thêm từng sản phẩm
        for (ProductOrderDto item : cartItems) {
            // Tạo CardView bao ngoài
            CardView card = new CardView(this);
            card.setCardElevation(4f);
            card.setRadius(24f);
            card.setUseCompatPadding(true);

            // Set margin
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            card.setLayoutParams(cardParams);

            // Tạo LinearLayout bên trong card
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setPadding(24, 16, 24, 16);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Tên sản phẩm
            TextView tvName = new TextView(this);
            tvName.setText(item.productName);
            tvName.setTextSize(16f);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // Giá
            TextView tvPrice = new TextView(this);
            tvPrice.setText("$" + String.format("%.2f", item.totalPrice));
            tvPrice.setTextSize(16f);
            tvPrice.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Gắn view
            rowLayout.addView(tvName);
            rowLayout.addView(tvPrice);
            card.addView(rowLayout);
            layoutProductItems.addView(card);
        }

        TextView tvTotal = findViewById(R.id.tv_cart_total);
        if (tvTotal != null) {
            tvTotal.setText("$" + String.format("%.2f", totalAmount));
        }


        // Chọn phương thức thanh toán
        LinearLayout paymentCard = findViewById(R.id.payment_card);
        LinearLayout paymentCOD = findViewById(R.id.payment_cod);

        ImageView checkCard = findViewById(R.id.check_card);
        ImageView checkCOD = findViewById(R.id.check_cod);

        // Hàm xử lý thay đổi lựa chọn
        View.OnClickListener onMethodSelected = v -> {
            // Reset icon về màu xám
            checkCard.setColorFilter(ContextCompat.getColor(this, R.color.gray));
            checkCOD.setColorFilter(ContextCompat.getColor(this, R.color.gray));

            // Gán màu cam cho lựa chọn
            if (v == paymentCard) {
                selectedPaymentMethod = "Credit/Debit card";
                checkCard.setColorFilter(ContextCompat.getColor(this, R.color.orange));

                // 👉 Chuyển qua ScanQRActivity
                Intent intent2 = new Intent(PaymentActivity.this, ScanQRActivity.class);
                intent2.putExtra("total_amount", totalAmount); // Gửi tổng tiền nếu cần
                startActivity(intent2);
            } else if (v == paymentCOD) {
                selectedPaymentMethod = "Cash On Delivery";
                checkCOD.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            }
        };

        // 🔥 GÁN LISTENER CHO CÁC PHƯƠNG THỨC
        paymentCard.setOnClickListener(onMethodSelected);
        paymentCOD.setOnClickListener(onMethodSelected);


        // Sự kiện nút Back
        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PaymentActivity.this, CartActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        // Sự kiện nút Place Order
        TextView btnCheckout = findViewById(R.id.btn_checkout);
        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedPaymentMethod == null || selectedPaymentMethod.isEmpty()) {
                    Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                if (userId == -1) {
                    Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }

                orderViewModel.placeOrder(
                        userId,
                        totalAmount,
                        selectedPaymentMethod,
                        cartItems,
                        () -> runOnUiThread(() -> {
                            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, OrderPlaceActivity.class));
                            finish();
                        }),
                        e -> runOnUiThread(() -> {
                            Toast.makeText(this, "Lỗi đặt hàng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        })
                );
            });
        }
    }
}