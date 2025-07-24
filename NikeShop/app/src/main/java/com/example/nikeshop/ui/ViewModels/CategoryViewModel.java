package com.example.nikeshop.ui.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.repositories.CategoryRepository;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository categoryRepository;
    private final LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application); // ✅ dùng application
        allCategories = categoryRepository.getAllCategories();
    }


    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}
