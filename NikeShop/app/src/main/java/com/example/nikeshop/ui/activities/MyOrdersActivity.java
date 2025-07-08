package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.R;
import com.example.nikeshop.adapter.OrdersAdapter;
import com.example.nikeshop.data.OrderDataManager;
import com.example.nikeshop.models.Order;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity implements OrdersAdapter.OnOrderClickListener {

    private ImageButton btnBack;
    private RecyclerView rvOrders;
    private LinearLayout navHome, navSearch, navFavourites, navProfile;

    private OrdersAdapter ordersAdapter;
    private OrderDataManager dataManager;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        rvOrders = findViewById(R.id.rv_orders);
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navFavourites = findViewById(R.id.nav_favourites);
        navProfile = findViewById(R.id.nav_profile);

        dataManager = OrderDataManager.getInstance();
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        // Initialize with empty list first
        ordersAdapter = new OrdersAdapter(this, new ArrayList<>());
        ordersAdapter.setOnOrderClickListener(this);
        rvOrders.setAdapter(ordersAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Navigation click listeners
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyOrdersActivity.this, "Navigate to Home", Toast.LENGTH_SHORT).show();
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyOrdersActivity.this, "Navigate to Search", Toast.LENGTH_SHORT).show();
            }
        });

        navFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyOrdersActivity.this, "Navigate to Favourites", Toast.LENGTH_SHORT).show();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyOrdersActivity.this, "You're in Profile section", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrders() {
        // Load orders from data manager (currently sample data)
        orderList = dataManager.getAllOrders();
        ordersAdapter.updateOrders(orderList);
    }

    @Override
    public void onOrderClick(Order order) {
        Toast.makeText(this, "Order clicked: " + order.getOrderNumber(), Toast.LENGTH_SHORT).show();
        // TODO: Open order details screen
    }

    // Method to refresh orders (for future use)
    public void refreshOrders() {
        loadOrders();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}