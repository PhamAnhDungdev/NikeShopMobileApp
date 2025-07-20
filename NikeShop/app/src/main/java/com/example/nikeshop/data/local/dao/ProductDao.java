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
    void insertAll(List<Product> products);
    // Thêm sản phẩm
    @Insert
    void insertProduct(Product product);

    // Lấy tất cả sản phẩm
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    // Tìm sản phẩm theo tên gần đúng (LIKE %keyword%)
    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%'")
    LiveData<List<Product>> getProductsByName(String keyword);

    // Lọc sản phẩm theo category id
    @Query("SELECT * FROM products WHERE category_id = :categoryId")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    // (Tuỳ chọn) Kết hợp cả tên và thể loại
    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%' AND category_id = :categoryId")
    LiveData<List<Product>> getProductsByNameAndCategory(String keyword, int categoryId);

    // Xóa sản phẩm
    @Delete
    void deleteProduct(Product product);

}
