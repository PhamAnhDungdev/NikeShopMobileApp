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

    @Query("SELECT * FROM reviews WHERE product_id = :productId ORDER BY review_date DESC")
    androidx.lifecycle.LiveData<List<Review>> getReviewsByProductId(int productId);

    @androidx.room.Query("SELECT * FROM reviews WHERE product_id = :productId")
    java.util.List<Review> getReviewsListByProductId(int productId);
    @Query("SELECT * FROM reviews")
List<Review> getAllReviews();
}
