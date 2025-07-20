package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CategoryDao;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.local.entity.Product;

import java.util.List;

public class CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDao = db.categoryDao();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void insert(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.insert(category));
    }

    public void delete(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.delete(category));
    }
    public void update(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDao.update(category));
    }
}
