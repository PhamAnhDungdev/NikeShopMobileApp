package com.example.nikeshop.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository repository;
    private final LiveData<List<Category>> categoryList;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        categoryList = repository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryList;
    }

    public void insert(Category category) {
        repository.insert(category);
    }

    public void delete(Category category) {
        repository.delete(category);
    }

    public void update(Category category) {
        repository.update(category);
    }
}
