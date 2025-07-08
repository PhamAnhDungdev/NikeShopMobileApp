package com.example.nikeshop.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.R;
import com.example.nikeshop.adapter.FavouriteProductAdapter;
import com.example.nikeshop.models.ProductFavourite;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView rvFavourites;
    private FavouriteProductAdapter adapter;
    private List<ProductFavourite> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);

        rvFavourites = findViewById(R.id.rvFavourites);

        // Dummy product data
        productList = new ArrayList<>();
        productList.add(new ProductFavourite("Nike Air 1", "Running Shoes", "$244.99", R.drawable.shoes1));
        productList.add(new ProductFavourite("Nike Air 444", "Casual Shoes", "$244.99", R.drawable.shoes1));

        adapter = new FavouriteProductAdapter(productList, this);
        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setAdapter(adapter);
    }
}