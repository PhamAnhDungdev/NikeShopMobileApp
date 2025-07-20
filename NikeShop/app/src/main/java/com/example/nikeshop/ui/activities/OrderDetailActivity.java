package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.Models.OrderViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Order;

import java.util.Arrays;
import java.util.Date;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderStatus, tvOrderDate, tvOrderTotal, tvPaymentMethod;

    private OrderViewModel orderViewModel;

    private Spinner spinnerStatus;
    private Button btnUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        spinnerStatus = findViewById(R.id.spinner_status);
        btnUpdate = findViewById(R.id.btn_update_order_status);

        tvOrderId = findViewById(R.id.tv_detail_order_id);
        tvOrderStatus = findViewById(R.id.tv_detail_order_status);
        tvOrderDate = findViewById(R.id.tv_detail_order_date);
        tvOrderTotal = findViewById(R.id.tv_detail_order_total);
        tvPaymentMethod = findViewById(R.id.tv_detail_payment_method);

        int orderId = getIntent().getIntExtra("order_id", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Các trạng thái bạn hỗ trợ
        String[] statuses = {"pending", "processing", "completed"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerStatus.setAdapter(adapter);

        orderViewModel.getOrderById(orderId).observe(this, order -> {
            if (order != null) {
                displayOrderInfo(order);

                int index = Arrays.asList(statuses).indexOf(order.getStatus().toLowerCase());
                spinnerStatus.setSelection(index >= 0 ? index : 0);

                btnUpdate.setOnClickListener(v -> {
                    String newStatus = spinnerStatus.getSelectedItem().toString();
                    order.setStatus(newStatus);
                    order.setUpdatedAt(new Date());

                    orderViewModel.update(order);
                    Toast.makeText(this, "Đã cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                Toast.makeText(this, "Đơn hàng không tồn tại!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void displayOrderInfo(Order order) {
        tvOrderId.setText("Mã đơn: #" + order.getId());
        tvOrderStatus.setText("Trạng thái: " + order.getStatus());
        tvOrderDate.setText("Ngày đặt: " + order.getOrderDate().toString());
        tvOrderTotal.setText("Tổng tiền: " + order.getTotalPrice() + " VND");
        tvPaymentMethod.setText("Thanh toán: " + order.getPaymentMethod());
    }
}
