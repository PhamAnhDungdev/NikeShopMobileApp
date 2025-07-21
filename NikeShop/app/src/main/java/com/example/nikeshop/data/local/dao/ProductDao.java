package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Product;

import java.util.List;


@Dao
public interface ProductDao {

    @Insert
    List<Long> insertAll(List<Product> products);

    @Insert
    void insertProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM products WHERE deleted_at IS NULL")
    int countProducts();

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%' AND deleted_at IS NULL")
    LiveData<List<Product>> getProductsByName(String keyword);

    @Query("SELECT * FROM products WHERE category_id = :categoryId AND deleted_at IS NULL")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%' AND category_id = :categoryId AND deleted_at IS NULL")
    LiveData<List<Product>> getProductsByNameAndCategory(String keyword, int categoryId);

    // Synchronous method for ViewModel
    @Query("SELECT * FROM products WHERE id = :productId AND deleted_at IS NULL")
    Product getProductById(int productId);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<Product>> getProductsByNameOrDescription(String query);

}
