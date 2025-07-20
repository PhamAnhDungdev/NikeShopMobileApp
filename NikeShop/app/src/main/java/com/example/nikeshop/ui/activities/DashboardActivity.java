package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.Models.OrderViewModel;
import com.example.nikeshop.Models.ProductViewModel;
import com.example.nikeshop.R;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalProducts, tvTotalOrders, tvDailyRevenue;
    private Button btnManageProducts, btnManageOrders, btnManageCustomers;

    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvTotalProducts = findViewById(R.id.tv_total_products);
        tvTotalOrders = findViewById(R.id.tv_total_orders);
        tvDailyRevenue = findViewById(R.id.tv_daily_revenue);

        btnManageProducts = findViewById(R.id.btn_manage_products);
        btnManageOrders = findViewById(R.id.btn_manage_orders);
        btnManageCustomers = findViewById(R.id.btn_manage_customers);

        // ViewModels
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Tổng số sản phẩm
        productViewModel.getAllProducts().observe(this, products ->
                tvTotalProducts.setText(String.valueOf(products.size()))
        );

        // Tổng số đơn hàng
        orderViewModel.getAllOrders().observe(this, orders -> {
            tvTotalOrders.setText(String.valueOf(orders.size()));

            // Tính tổng doanh thu hôm nay (demo logic)
            double todayRevenue = 0;
            for (var order : orders) {
                // Giả định orderDate là hôm nay
                if (/* check if today */ true) {
                    todayRevenue += order.getTotalPrice();
                }
            }
            tvDailyRevenue.setText(String.format("%,.0f VND", todayRevenue));
        });

        // Chuyển trang
        btnManageProducts.setOnClickListener(v ->
                startActivity(new Intent(this, ProductManagementActivity.class))
        );

        btnManageOrders.setOnClickListener(v ->
                startActivity(new Intent(this, OrderManagementActivity.class))
        );

        btnManageCustomers.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng sắp ra mắt!", Toast.LENGTH_SHORT).show()
        );

        Button btnCreateOrder = findViewById(R.id.btn_create_order);
        btnCreateOrder.setOnClickListener(v ->
                Toast.makeText(this, "Admin không thể tạo đơn hàng", Toast.LENGTH_SHORT).show()
        );
    }
}
