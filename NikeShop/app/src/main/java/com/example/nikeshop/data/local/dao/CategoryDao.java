package com.example.nikeshop.data.local.dao;

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
}
