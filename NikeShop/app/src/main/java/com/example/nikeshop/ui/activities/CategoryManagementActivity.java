package com.example.nikeshop.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.Models.CategoryViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.CategoryAdapter;
import com.example.nikeshop.data.local.entity.Category;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryManagementActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryActionListener {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private Button btnAdd;
    private ImageButton btnBack;

    private CategoryAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        recyclerView = findViewById(R.id.recycler_category);
        etSearch = findViewById(R.id.et_search_category);
        btnAdd = findViewById(R.id.btn_add_category);
        btnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(this, categoryList, this);
        recyclerView.setAdapter(adapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            adapter.notifyDataSetChanged();
        });

        btnAdd.setOnClickListener(v -> openAddEditCategory(null));
        btnBack.setOnClickListener(v -> onBackPressed());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                List<Category> filtered = new ArrayList<>();
                for (Category c : categoryList) {
                    if (c.getName().toLowerCase().contains(query)) {
                        filtered.add(c);
                    }
                }
                adapter = new CategoryAdapter(CategoryManagementActivity.this, filtered, CategoryManagementActivity.this);
                recyclerView.setAdapter(adapter);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void openAddEditCategory(Category category) {
        Intent intent = new Intent(this, AddEditCategoryActivity.class);
        if (category != null) {
            intent.putExtra("category", category);
        }
        startActivity(intent);
    }

    @Override
    public void onEdit(Category category) {
        openAddEditCategory(category);
    }

    @Override
    public void onDelete(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá danh mục")
                .setMessage("Bạn có chắc muốn xoá danh mục này?")
                .setPositiveButton("Xoá", (dialog, which) -> categoryViewModel.delete(category))
                .setNegativeButton("Hủy", null)
                .show();
    }
}
