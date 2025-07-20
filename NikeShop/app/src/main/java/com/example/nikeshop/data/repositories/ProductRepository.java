package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Product;

import java.util.List;

public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        productDao = db.productDao();
    }

    public LiveData<List<Product>> getAllProducts() {
        return productDao.getAllActiveProducts();
    }

    public void insert(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> productDao.insert(product));
    }

    public void update(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> productDao.update(product));
    }

    public void delete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> productDao.delete(product));
    }

    public LiveData<List<Product>> search(String query) {
        return productDao.searchProducts(query);
    }

    public LiveData<List<Product>> getByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }
}
