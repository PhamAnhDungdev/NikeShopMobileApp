package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.R;
import com.example.nikeshop.adapters.FavouriteProductAdapter;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.ui.ViewModels.CartViewModel;
import com.example.nikeshop.ui.ViewModels.WishlistViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends BottomMenuActivity {

    private RecyclerView rvFavourites;
    private FavouriteProductAdapter adapter;
    private WishlistViewModel viewModel;
    private List<Product> productList = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        setupBottomMenu(R.id.nav_favourites);

        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);
        ImageView btnCart = topNavBar.findViewById(R.id.btn_cart);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Bạn cần đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rvFavourites = findViewById(R.id.rvFavourites);
        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        adapter = new FavouriteProductAdapter(productList, this, cartViewModel);
        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setAdapter(adapter);

        // Load danh sách yêu thích
        viewModel.getWishlistProducts(userId).observe(this, products -> {
            productList.clear();
            productList.addAll(products);
            adapter.setProducts(productList);
        });

        // Nút quay về
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Nút giỏ hàng
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(FavouriteActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện xóa yêu thích
        adapter.setOnDeleteClickListener((position, product) -> {
            new AlertDialog.Builder(FavouriteActivity.this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này khỏi yêu thích?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        viewModel.removeFromWishlist(userId, product.getId());

                        productList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, productList.size());

                        Toast.makeText(FavouriteActivity.this, product.getName() + " đã xoá khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }
}
