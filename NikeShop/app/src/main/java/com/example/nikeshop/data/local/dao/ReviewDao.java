package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    List<Long> insertAll(List<Review> reviews);
    @Query("SELECT COUNT(*) FROM reviews")
    int countReviews();
}
