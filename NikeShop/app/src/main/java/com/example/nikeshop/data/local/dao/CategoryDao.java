package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    List<Long> insertAll(List<Category> categories);

    @Query("SELECT COUNT(*) FROM categories")
    int countCategories();

    @Query("SELECT * FROM categories")
    androidx.lifecycle.LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category getCategoryById(int id);

    @Insert
    void insertCategory(Category category);

    @Insert
    void insert(Category category);

    @androidx.room.Delete
    void delete(Category category);

    @androidx.room.Update
    void update(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAllNow();


}
