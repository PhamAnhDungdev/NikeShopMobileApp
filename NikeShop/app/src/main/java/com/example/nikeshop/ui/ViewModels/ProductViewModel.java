package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.repositories.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = NikeShopApp.getDatabase();
        productRepository = new ProductRepository(db.productDao());
        allProducts = productRepository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> searchProductsByName(String keyword) {
        return productRepository.getProductsByName(keyword);
    }

    public LiveData<List<Product>> getProductsByCategory(int categoryId) {
        return productRepository.getProductsByCategory(categoryId);
    }

    public LiveData<List<Product>> getProductsByNameAndCategory(String keyword, int categoryId) {
        return productRepository.getProductsByNameAndCategory(keyword, categoryId);
    }

    public void insertProduct(Product product) {
        productRepository.insertProduct(product);
    }

    public void deleteProduct(Product product) {
        productRepository.deleteProduct(product);
    }
}
