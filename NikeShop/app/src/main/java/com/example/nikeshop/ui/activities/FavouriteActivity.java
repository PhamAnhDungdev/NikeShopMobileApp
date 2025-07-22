package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.NikeShopApp;
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

    private static final int CURRENT_USER_ID = 2; // TODO: replace with actual user session ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        setupBottomMenu(R.id.nav_favourites);

        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);
        ImageView btnCart = topNavBar.findViewById(R.id.btn_cart);

        rvFavourites = findViewById(R.id.rvFavourites);
        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        adapter = new FavouriteProductAdapter(new ArrayList<>(), this, cartViewModel);

        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        viewModel.getWishlistProducts(prefs.getInt("user_id", -1)).observe(this, products -> {
            Log.d("DEBUG_WISHLIST", "Fetched wishlist products: " + products.size());
            for (Product p : products) {
                Log.d("DEBUG_WISHLIST", p.getName());
            }
            adapter.setProducts(products);
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavouriteActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
