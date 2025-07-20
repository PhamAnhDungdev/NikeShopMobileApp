package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.nikeshop.data.local.entity.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insertAll(List<Review> reviews);
}
