package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.OrderDetailAdapter;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.ui.ViewModels.OrderDetailViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailAdapter.OnRatingClickListener {
    private ImageButton btnBack;
    private TextView tvOrderNumber;
    private TextView tvOrderStatus;
    private TextView tvPlacedDate;
    private TextView tvTotalAmount;
    private TextView tvSubtotal;
    private TextView tvShipping;
    private TextView tvFinalTotal;
    private RecyclerView rvOrderDetails;

    private OrderDetailAdapter orderDetailAdapter;
    private OrderDetailViewModel orderDetailViewModel;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get order ID from intent
        Intent intent = getIntent();
        orderId = intent.getIntExtra("ORDER_ID", -1);

        if (orderId == -1) {
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupClickListeners();
        setupViewModel();
        loadOrderDetails();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvOrderNumber = findViewById(R.id.tv_order_number);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvPlacedDate = findViewById(R.id.tv_placed_date);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        rvOrderDetails = findViewById(R.id.rv_order_details);
    }

    private void setupRecyclerView() {
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        orderDetailAdapter = new OrderDetailAdapter(this, new ArrayList<>());
        orderDetailAdapter.setOnRatingClickListener(this);
        rvOrderDetails.setAdapter(orderDetailAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupViewModel() {
        orderDetailViewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);

        // Observe order details
        orderDetailViewModel.getCurrentOrder().observe(this, this::updateOrderInfo);
        orderDetailViewModel.getOrderDetailsWithProducts().observe(this, orderDetails -> {
            orderDetailAdapter.updateOrderDetails(orderDetails);
            calculateTotals(orderDetails);
        });
    }

    private void loadOrderDetails() {
        orderDetailViewModel.loadOrderDetails(orderId);
    }

    private void updateOrderInfo(Order order) {
        if (order != null) {
            tvOrderNumber.setText("Order #" + order.getId());
            tvOrderStatus.setText(order.getStatus());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
            if (order.getOrderDate() != null) {
                tvPlacedDate.setText(dateFormat.format(order.getOrderDate()));
            }

            tvTotalAmount.setText(String.format("$%.2f", order.getTotalPrice()));
            tvFinalTotal.setText(String.format("$%.2f", order.getTotalPrice()));
        }
    }

    private void calculateTotals(java.util.List<OrderDetailAdapter.OrderDetailWithProduct> orderDetails) {
        if (orderDetails != null && !orderDetails.isEmpty()) {
            double subtotal = 0;
            for (OrderDetailAdapter.OrderDetailWithProduct item : orderDetails) {
                subtotal += item.orderDetail.getPrice() * item.orderDetail.getQuantity();
            }

            tvSubtotal.setText(String.format("$%.2f", subtotal));
            // Assuming free shipping for now
            tvShipping.setText("Free");
            tvFinalTotal.setText(String.format("$%.2f", subtotal));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRatingClick(OrderDetail orderDetail, Product product) {
        // Navigate to Rating Activity
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("ORDER_DETAIL_ID", orderDetail.getId());
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("PRODUCT_NAME", product.getName());
        intent.putExtra("ORDER_ID", orderId);
        startActivity(intent);
    }
}
