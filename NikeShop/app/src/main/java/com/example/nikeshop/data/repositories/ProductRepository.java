package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
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

    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    public LiveData<List<Product>> getProductsByNameOrDescription(String query) {
        return productDao.getProductsByNameOrDescription(query);
    }

    public void insert(Product product) {
        executorService.execute(() -> productDao.insertProduct(product));
    }

    public void update(Product product) {
        executorService.execute(() -> {
            productDao.deleteProduct(product); // Xoá cũ
            productDao.insertProduct(product); // Thêm mới lại như update đơn giản
        });
    }

    public void delete(Product product) {
        executorService.execute(() -> productDao.deleteProduct(product));
    }

    public LiveData<List<Product>> search(String query) {
        return productDao.getProductsByNameOrDescription(query);
    }

    public LiveData<List<Product>> getByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public void insertProduct(Product product) {
    }

    public void deleteProduct(Product product) {
    }
}
