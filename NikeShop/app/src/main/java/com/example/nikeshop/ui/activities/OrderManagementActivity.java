package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.Models.OrderViewModel;
import com.example.nikeshop.R;

import com.example.nikeshop.adapters.OrdersAdapter;
import com.example.nikeshop.data.local.entity.Order;


import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity implements OrdersAdapter.OnOrderActionListener {

    private EditText etSearch;
    private RecyclerView recyclerOrders;
    private ImageButton btnBack;

    private OrderViewModel orderViewModel;
    private OrdersAdapter ordersAdapter;
    private List<Order> currentOrders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        // Khớp với layout XML
        etSearch = findViewById(R.id.et_order_search);
        recyclerOrders = findViewById(R.id.recycler_orders);
        btnBack = findViewById(R.id.btn_back_orders);

        // Khởi tạo adapter với danh sách rỗng
        ordersAdapter = new OrdersAdapter(this, currentOrders);
        ordersAdapter.setOnOrderActionListener(this);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrders.setAdapter(ordersAdapter);

        // ViewModel
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(this, orders -> {
            currentOrders = orders;
            ordersAdapter.updateOrders(currentOrders);
        });

        // Tìm kiếm đơn hàng
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                orderViewModel.search(s.toString()).observe(OrderManagementActivity.this, filtered -> {
                    ordersAdapter.updateOrders(filtered);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        Button btnAll = findViewById(R.id.btn_all_orders);
        Button btnPending = findViewById(R.id.btn_pending_orders);
        Button btnProcessing = findViewById(R.id.btn_processing_orders);
        Button btnCompleted = findViewById(R.id.btn_completed_orders);

        btnAll.setOnClickListener(v -> orderViewModel.getAllOrders().observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnPending.setOnClickListener(v -> orderViewModel.getOrdersByStatus("pending").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnProcessing.setOnClickListener(v -> orderViewModel.getOrdersByStatus("processing").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnCompleted.setOnClickListener(v -> orderViewModel.getOrdersByStatus("completed").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

    }

    @Override
    public void onView(Order order) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_id", order.getId());
        startActivity(intent);
    }


    @Override
    public void onView(com.example.nikeshop.models.Order order) {

    }

    @Override
    public void onDelete(com.example.nikeshop.models.Order order) {

    }

    @Override
    public void onDelete(Order order) {
        String[] statuses = {"pending", "processing", "completed"};
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái")
                .setItems(statuses, (dialog, which) -> {
                    order.setStatus(statuses[which]);
                    orderViewModel.update(order);
                    Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                }).show();
    }

}
