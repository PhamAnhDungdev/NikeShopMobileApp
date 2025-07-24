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

import com.example.nikeshop.Models.OrderDisplayItem;
import com.example.nikeshop.Models.OrderViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.OrderDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity implements OrderDisplayAdapter.OnOrderClickListener {

    private EditText etSearch;
    private RecyclerView recyclerOrders;
    private ImageButton btnBack;

    private OrderViewModel orderViewModel;
    private OrderDisplayAdapter ordersAdapter;
    private List<OrderDisplayItem> currentOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        etSearch = findViewById(R.id.et_order_search);
        recyclerOrders = findViewById(R.id.recycler_orders);
        btnBack = findViewById(R.id.btn_back_orders);

        ordersAdapter = new OrderDisplayAdapter(this, currentOrders);
        ordersAdapter.setOnOrderClickListener(this);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrders.setAdapter(ordersAdapter);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrderDisplayItems().observe(this, orders -> {
            currentOrders = orders;
            ordersAdapter.updateOrders(currentOrders);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                orderViewModel.searchDisplayItems(s.toString()).observe(OrderManagementActivity.this, filtered -> {
                    ordersAdapter.updateOrders(filtered);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnBack.setOnClickListener(v -> onBackPressed());

        Button btnAll = findViewById(R.id.btn_all_orders);
        Button btnPending = findViewById(R.id.btn_pending_orders);
        Button btnProcessing = findViewById(R.id.btn_processing_orders);
        Button btnCompleted = findViewById(R.id.btn_completed_orders);

        btnAll.setOnClickListener(v -> orderViewModel.getOrderDisplayItems().observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnPending.setOnClickListener(v -> orderViewModel.getDisplayItemsByStatus("pending").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnProcessing.setOnClickListener(v -> orderViewModel.getDisplayItemsByStatus("processing").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));

        btnCompleted.setOnClickListener(v -> orderViewModel.getDisplayItemsByStatus("completed").observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        }));
    }

    @Override
    public void onOrderClick(OrderDisplayItem order) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_id", order.getOrderId());
        startActivity(intent);
    }
}