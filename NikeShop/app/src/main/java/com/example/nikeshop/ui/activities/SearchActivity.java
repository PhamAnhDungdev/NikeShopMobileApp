package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.adapters.ProductSuggestionAdapter;
import com.example.nikeshop.R;
import com.example.nikeshop.ui.ViewModels.ProductViewModel;

import java.util.ArrayList;

public class SearchActivity extends BottomMenuActivity {
    private EditText etSearch;
    private RecyclerView rvSuggestions;
    private ProductSuggestionAdapter adapter;
    private ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        rvSuggestions = findViewById(R.id.rvSuggestions);

        // Adapter
        adapter = new ProductSuggestionAdapter(product -> {
            Toast.makeText(this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
            // Chuyển sang ProductDetailActivity
            android.content.Intent intent = new android.content.Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });

        rvSuggestions.setAdapter(adapter);
        rvSuggestions.setLayoutManager(new LinearLayoutManager(this));

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // ✅ Observe 1 lần duy nhất
        viewModel.searchResults.observe(this, adapter::submitList);

        // ✅ Cập nhật query qua ViewModel
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                viewModel.setSearchQuery(query); // luôn gọi, vì Transformations sẽ xử lý rỗng rồi
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

}
