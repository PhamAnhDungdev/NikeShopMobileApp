package com.example.nikeshop.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResult;


import com.example.nikeshop.Models.CategoryViewModel;
import com.example.nikeshop.Models.ProductViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.adapters.ProductAdminAdapter;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.local.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductAdminAdapter.OnProductActionListener {

    private ProductViewModel productViewModel;
    private ProductAdminAdapter adapter;
    private List<Product> currentProducts = new ArrayList<>();

    private ActivityResultLauncher<Intent> addEditProductLauncher;


    private EditText etSearch;
    private Spinner spinnerCategory;
    private RecyclerView recyclerView;
    private Button btnAddProduct;
    private ImageButton btnBack;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        setupViews();
        setupViewModel();
        setupRecyclerView();
        setupListeners();

        addEditProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        productViewModel.getAllProducts().observe(this, products -> {
                            adapter = new ProductAdminAdapter(this, products, this);
                            recyclerView.setAdapter(adapter);
                        });
                    }
                }
        );

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;

            // Spinner adapter
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    categoryList
            );
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);
        });


    }

    private void setupViews() {
        etSearch = findViewById(R.id.et_search);
        spinnerCategory = findViewById(R.id.spinner_category);
        recyclerView = findViewById(R.id.recycler_products_admin);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            currentProducts = products;
            adapter = new ProductAdminAdapter(this, currentProducts, this);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProductActivity.class);
            addEditProductLauncher.launch(intent);
        });



        btnBack.setOnClickListener(v -> onBackPressed());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                productViewModel.search(s.toString()).observe(ProductManagementActivity.this, filtered -> {
                    adapter = new ProductAdminAdapter(ProductManagementActivity.this, filtered, ProductManagementActivity.this);
                    recyclerView.setAdapter(adapter);
                });
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // TODO: Xử lý lọc theo spinnerCategory nếu bạn có Category entity + adapter
    }

    @Override
    public void onEdit(Product product) {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra("product", product);
        addEditProductLauncher.launch(intent);
    }



    @Override
    public void onDelete(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> productViewModel.delete(product))
                .setNegativeButton("Hủy", null)
                .show();
    }
}
