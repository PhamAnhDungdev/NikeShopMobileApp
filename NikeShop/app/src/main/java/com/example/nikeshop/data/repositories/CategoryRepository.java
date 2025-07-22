package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.nikeshop.data.local.dao.CategoryDao;
import com.example.nikeshop.data.local.entity.Category;
import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}

