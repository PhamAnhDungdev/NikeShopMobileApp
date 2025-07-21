package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.util.Log;

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

        rvFavourites = findViewById(R.id.rvFavourites);
        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        adapter = new FavouriteProductAdapter(new ArrayList<>(), this, cartViewModel);

        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        viewModel.getWishlistProducts(CURRENT_USER_ID).observe(this, products -> {
            Log.d("DEBUG_WISHLIST", "Fetched wishlist products: " + products.size());
            for (Product p : products) {
                Log.d("DEBUG_WISHLIST", p.getName());
            }
            adapter.setProducts(products);
        });
    }
}
