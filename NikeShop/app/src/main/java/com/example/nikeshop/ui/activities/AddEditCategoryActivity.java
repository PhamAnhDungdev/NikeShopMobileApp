package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.Models.CategoryViewModel;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Category;


import java.util.Date;

public class AddEditCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;
    private Button btnSave;
    private Category editingCategory;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        etCategoryName = findViewById(R.id.et_category_name);
        btnSave = findViewById(R.id.btn_save_category);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        if (getIntent().hasExtra("category")) {
            editingCategory = (Category) getIntent().getSerializableExtra("category");
            etCategoryName.setText(editingCategory.getName());
        }

        btnSave.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                etCategoryName.setError("Vui lòng nhập tên danh mục");
                return;
            }

            if (editingCategory == null) {
                Category newCategory = new Category(name, new Date(), new Date(), null);
                categoryViewModel.insert(newCategory);
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            } else {
                editingCategory.setName(name);
                editingCategory.setUpdatedAt(new Date());
                categoryViewModel.update(editingCategory);
                Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
