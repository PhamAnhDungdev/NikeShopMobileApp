package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.OrdersAdapter;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.repositories.OrderRepository;
import com.example.nikeshop.ui.ViewModels.OrderViewModel;
import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity implements OrdersAdapter.OnOrderClickListener {
    private ImageButton btnBack;
    private RecyclerView rvOrders;
    private LinearLayout navHome, navSearch, navFavourites, navProfile;
    private OrdersAdapter ordersAdapter;
    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        observeOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        rvOrders = findViewById(R.id.rv_orders);
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navFavourites = findViewById(R.id.nav_favourites);
        navProfile = findViewById(R.id.nav_profile);

        // Tạo ViewModel qua Factory (nếu muốn truyền Repository vào ViewModel)
        OrderDao orderDao = AppDatabase.getInstance(getApplicationContext()).orderDao();
        OrderRepository repository = new OrderRepository(orderDao);
        orderViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(OrderViewModel.class);
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        ordersAdapter = new OrdersAdapter(this, new ArrayList<>());
        ordersAdapter.setOnOrderClickListener(this);
        rvOrders.setAdapter(ordersAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        navHome.setOnClickListener(v -> Toast.makeText(this, "Navigate to Home", Toast.LENGTH_SHORT).show());
        navSearch.setOnClickListener(v -> Toast.makeText(this, "Navigate to Search", Toast.LENGTH_SHORT).show());
        navFavourites.setOnClickListener(v -> Toast.makeText(this, "Navigate to Favourites", Toast.LENGTH_SHORT).show());
        navProfile.setOnClickListener(v -> Toast.makeText(this, "You're in Profile section", Toast.LENGTH_SHORT).show());
    }

    private void observeOrders() {
        orderViewModel.getAllOrders().observe(this, orders -> {
            ordersAdapter.updateOrders(orders);
        });
    }

    @Override
    public void onOrderClick(Order order) {
        // Navigate to OrderDetailActivity with order ID
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("ORDER_ID", order.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
