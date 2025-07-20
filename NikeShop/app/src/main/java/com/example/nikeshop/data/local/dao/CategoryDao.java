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
    void insert(Category category);

    @androidx.room.Delete
    void delete(Category category);

    @androidx.room.Update
    void update(Category category);

    @Query("SELECT * FROM categories WHERE deleted_at IS NULL ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories")
    List<Category> getAllNow();


}
