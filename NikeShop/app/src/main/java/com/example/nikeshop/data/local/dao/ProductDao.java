package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.nikeshop.data.local.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products WHERE deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Product>> getAllActiveProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' AND deleted_at IS NULL")
    LiveData<List<Product>> searchProducts(String query);

    @Query("SELECT * FROM products WHERE category_id = :categoryId AND deleted_at IS NULL")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products")
    List<Product> getAllNow();
}

