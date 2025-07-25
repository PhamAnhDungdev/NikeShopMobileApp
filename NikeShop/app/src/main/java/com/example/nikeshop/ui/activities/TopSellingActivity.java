package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.Models.TopSellingViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.TopSellingAdapter;

public class TopSellingActivity extends AppCompatActivity {

    private RecyclerView recyclerTopSale;
    private TopSellingViewModel topSellingViewModel;
    private TopSellingAdapter topSellingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_selling);

        recyclerTopSale = findViewById(R.id.recycler_top_selling);
        recyclerTopSale.setLayoutManager(new LinearLayoutManager(this));

        topSellingAdapter = new TopSellingAdapter(this);
        recyclerTopSale.setAdapter(topSellingAdapter);

        topSellingViewModel = new ViewModelProvider(this).get(TopSellingViewModel.class);
        topSellingViewModel.getTopSellingProducts(10).observe(this, products -> {
            topSellingAdapter.setProducts(products);
        });
    }
}
