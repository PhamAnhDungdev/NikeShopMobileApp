package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {

    private final ProductDao productDao;
    private final ExecutorService executorService;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getAllProducts() {
        return productDao.getAllProducts();
    }

    public LiveData<List<Product>> getProductsByName(String keyword) {
        return productDao.getProductsByName(keyword);
    }

    public LiveData<List<Product>> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public LiveData<List<Product>> getProductsByNameAndCategory(String keyword, int categoryId) {
        return productDao.getProductsByNameAndCategory(keyword, categoryId);
    }

    public void insertProduct(Product product) {
        executorService.execute(() -> productDao.insertProduct(product));
    }

    public void deleteProduct(Product product) {
        executorService.execute(() -> productDao.deleteProduct(product));
    }
}
