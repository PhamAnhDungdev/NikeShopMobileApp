package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailAdapter.OnRatingClickListener {
    private static final String TAG = "OrderDetailActivity";

    private ImageButton btnBack;
    private TextView tvOrderNumber;
    private TextView tvOrderStatus;
    private TextView tvPlacedDate;
    private TextView tvSubtotal;
    private RecyclerView rvOrderDetails;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private OrderDetailAdapter orderDetailAdapter;
    private OrderDetailViewModel orderDetailViewModel;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Lấy order ID từ intent
        Intent intent = getIntent();
        orderId = intent.getIntExtra("ORDER_ID", -1);

        Log.d(TAG, "Nhận Order ID: " + orderId);

        if (orderId == -1) {
            Log.e(TAG, "Order ID không hợp lệ");
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

        // Thêm progress bar và empty message nếu có trong layout
        // progressBar = findViewById(R.id.progress_bar);
        // tvEmptyMessage = findViewById(R.id.tv_empty_message);
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

        // Observe danh sách order details
        orderDetailViewModel.getOrderDetailsWithProducts().observe(this, orderDetails -> {
            Log.d(TAG, "Nhận được order details: " + (orderDetails != null ? orderDetails.size() : 0) + " items");

            if (orderDetails != null && !orderDetails.isEmpty()) {
                rvOrderDetails.setVisibility(View.VISIBLE);
                // if (tvEmptyMessage != null) tvEmptyMessage.setVisibility(View.GONE);
                orderDetailAdapter.updateOrderDetails(orderDetails);
            } else {
                Log.w(TAG, "Không tìm thấy order details hoặc danh sách rỗng");
                rvOrderDetails.setVisibility(View.GONE);
                // if (tvEmptyMessage != null) tvEmptyMessage.setVisibility(View.VISIBLE);
            }
        });

        // Observe tổng tiền
        orderDetailViewModel.getTotalAmount().observe(this, total -> {
            if (total != null) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
            }
        });

        // Observe loading state
        orderDetailViewModel.getIsLoading().observe(this, isLoading -> {
            // if (progressBar != null) {
            //     progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            // }
        });
    }

    private void loadOrderDetails() {
        Log.d(TAG, "Đang load order details cho order ID: " + orderId);
        orderDetailViewModel.loadOrderDetails(orderId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRatingClick(OrderDetail orderDetail, Product product) {
        // Chuyển đến Rating Activity
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("ORDER_DETAIL_ID", orderDetail.getId());
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("PRODUCT_NAME", product.getName());
        intent.putExtra("ORDER_ID", orderId);
        startActivity(intent);
    }
}
