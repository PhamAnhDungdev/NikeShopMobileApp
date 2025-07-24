package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.nikeshop.Models.CategoryViewModel;
import com.example.nikeshop.Models.ProductViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.local.entity.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private Product editingProduct;

    private EditText etName, etDescription, etPrice, etSize, etStock, etImageUrl,
            etBrand, etColor, etMaterial, etSku;
    private Button btnSave;

    private CategoryViewModel categoryViewModel;
    private Spinner spinnerCategory;
    private List<Category> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        initViews();
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        if (getIntent().hasExtra("product")) {
            editingProduct = (Product) getIntent().getSerializableExtra("product");
            bindDataToForm(editingProduct);
        }

        btnSave.setOnClickListener(v -> {
            if (validate()) {
                Category selected = (Category) spinnerCategory.getSelectedItem();
                if (selected == null) {
                    Toast.makeText(this, "Vui lòng chọn danh mục trước khi lưu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editingProduct == null) {
                    Product newProduct = getProductFromForm();
                    newProduct.setCreatedAt(new Date());
                    productViewModel.insert(newProduct);
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Product updated = getProductFromForm();
                    updated.setId(editingProduct.getId());
                    updated.setCreatedAt(editingProduct.getCreatedAt());
                    updated.setUpdatedAt(new Date());
                    productViewModel.update(updated);
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
            }
        });


        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;

            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    categoryList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);

            // Nếu đang sửa sản phẩm → set lại danh mục đã chọn
            if (editingProduct != null) {
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId() == editingProduct.getCategoryId()) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
        });

        categoryViewModel.getAllCategories().observe(this, categories -> {
            Log.d("SPINNER_DEBUG", "Loaded category count: " + categories.size());
            for (Category c : categories) {
                Log.d("SPINNER_DEBUG", "Category: " + c.getName() + ", ID: " + c.getId());
            }


            if (categories.isEmpty()) {
                Toast.makeText(this, "Không có danh mục nào!", Toast.LENGTH_LONG).show();
                return;
            }

            categoryList = categories;

            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    categoryList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
        });



    }

    private void initViews() {
        spinnerCategory = findViewById(R.id.spinner_category);
        etName = findViewById(R.id.et_product_name);
        etDescription = findViewById(R.id.et_product_description);
        etPrice = findViewById(R.id.et_product_price);
        etSize = findViewById(R.id.et_product_size);
        etStock = findViewById(R.id.et_product_stock);
        etImageUrl = findViewById(R.id.et_product_image);
        etBrand = findViewById(R.id.et_product_brand);
        etColor = findViewById(R.id.et_product_color);
        etMaterial = findViewById(R.id.et_product_material);
        etSku = findViewById(R.id.et_product_sku);
        btnSave = findViewById(R.id.btn_save_product);
    }

    private void bindDataToForm(Product p) {
        etName.setText(p.getName());
        etDescription.setText(p.getDescription());
        etPrice.setText(String.valueOf(p.getPrice()));
        etSize.setText(p.getSize());
        etStock.setText(String.valueOf(p.getStockQuantity()));
        etImageUrl.setText(p.getImageUrl());
        etBrand.setText(p.getBrand());
        etColor.setText(p.getColor());
        etMaterial.setText(p.getMaterial());
        etSku.setText(p.getSku());
    }

    private Product getProductFromForm() {
        Category selected = (Category) spinnerCategory.getSelectedItem();
        int categoryId = selected != null ? selected.getId() : -1; // hoặc throw nếu null

        return new Product(
                etName.getText().toString().trim(),
                etDescription.getText().toString().trim(),
                Double.parseDouble(etPrice.getText().toString().trim()),
                etSize.getText().toString().trim(),
                Integer.parseInt(etStock.getText().toString().trim()),
                etImageUrl.getText().toString().trim(),
                categoryId,
                etBrand.getText().toString().trim(),
                etColor.getText().toString().trim(),
                etMaterial.getText().toString().trim(),
                etSku.getText().toString().trim(),
                null, null, null
        );
    }



    private boolean validate() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Vui lòng nhập tên sản phẩm");
            return false;
        }
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Vui lòng nhập giá");
            return false;
        }
        return true;
    }
}
