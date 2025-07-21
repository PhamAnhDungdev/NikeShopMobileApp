package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nikeshop.data.local.entity.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    Long insert(Review review);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);
    @Insert
    List<Long> insertAll(List<Review> reviews);
    @Query("SELECT * FROM reviews WHERE deleted_at IS NULL ORDER BY review_date DESC")
    LiveData<List<Review>> getAllReviews();

    @Query("SELECT * FROM reviews WHERE product_id = :productId AND deleted_at IS NULL ORDER BY review_date DESC")
    LiveData<List<Review>> getReviewsByProduct(int productId);

    @Query("SELECT * FROM reviews WHERE user_id = :userId AND deleted_at IS NULL ORDER BY review_date DESC")
    LiveData<List<Review>> getReviewsByUser(int userId);

    @Query("SELECT * FROM reviews WHERE user_id = :userId AND product_id = :productId AND deleted_at IS NULL")
    Review getReviewByUserAndProduct(int userId, int productId);

    @Query("SELECT AVG(rating) FROM reviews WHERE product_id = :productId AND deleted_at IS NULL")
    Double getAverageRatingForProduct(int productId);

    @Query("SELECT COUNT(*) FROM reviews WHERE product_id = :productId AND deleted_at IS NULL")
    int getReviewCountForProduct(int productId);

    @Query("SELECT COUNT(*) FROM reviews")
    int countReviews();
    @Query("DELETE FROM reviews")
    void deleteAll();
}
