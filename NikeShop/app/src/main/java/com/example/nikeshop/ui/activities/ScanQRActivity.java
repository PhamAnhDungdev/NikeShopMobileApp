package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.nikeshop.R;

import java.text.NumberFormat;
import java.util.Locale;

public class ScanQRActivity extends AppCompatActivity {
    private double totalAmount = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qractivity);

        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQRActivity.this, PaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        ImageView ivQr = findViewById(R.id.imageView);
        Button btnConfirm = findViewById(R.id.btnConfirmPayment);

        Intent intent = getIntent();
        totalAmount = intent.getDoubleExtra("total_amount", 0.0);

        // Hiển thị tổng tiền
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
        if (tvTotalAmount != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalAmount.setText("Tổng tiền: " + formatter.format(totalAmount));
        }


        String qrUrl = "https://img.vietqr.io/image/MB-190102038888-compact.png"
                + "?amount=" + (int)totalAmount
                + "&addInfo=Thanh%20toan%20don%20hang%20DH12345";

        // Load QR code vào ImageView
        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.drawable.qr1)
                .into(ivQr);

        btnConfirm.setOnClickListener(v -> {
            // Hiển thị Toast
            Toast.makeText(ScanQRActivity.this, "Thank you for your payment! We will confirm later.", Toast.LENGTH_SHORT).show();

            // Quay lại màn hình trước
            finish();
        });
    }
}