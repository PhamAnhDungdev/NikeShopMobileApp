package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.nikeshop.data.local.dao.ReviewDao;
import com.example.nikeshop.data.local.entity.Review;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewRepository {
    private final ReviewDao reviewDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ReviewRepository(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }


    public LiveData<List<Review>> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    public LiveData<List<Review>> getReviewsByProduct(int productId) {
        return reviewDao.getReviewsByProduct(productId);
    }

    public LiveData<List<Review>> getReviewsByUser(int userId) {
        return reviewDao.getReviewsByUser(userId);
    }

    public Review getReviewByUserAndProduct(int userId, int productId) {
        return reviewDao.getReviewByUserAndProduct(userId, productId);
    }

    public Double getAverageRatingForProduct(int productId) {
        return reviewDao.getAverageRatingForProduct(productId);
    }

    public int getReviewCountForProduct(int productId) {
        return reviewDao.getReviewCountForProduct(productId);
    }

    public void insertReview(Review review, OnReviewInsertedListener listener) {
        executor.execute(() -> {
            try {
                Long reviewId = reviewDao.insert(review);
                if (listener != null) {
                    listener.onSuccess(reviewId);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void updateReview(Review review, OnReviewUpdatedListener listener) {
        executor.execute(() -> {
            try {
                reviewDao.update(review);
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void deleteReview(Review review) {
        executor.execute(() -> reviewDao.delete(review));
    }

    public void deleteAll() {
        executor.execute(reviewDao::deleteAll);
    }

    // Callback interfaces
    public interface OnReviewInsertedListener {
        void onSuccess(Long reviewId);
        void onError(String error);
    }

    public interface OnReviewUpdatedListener {
        void onSuccess();
        void onError(String error);
    }
    public LiveData<List<Review>> getReviewsByProductId(int productId) {
        return reviewDao.getReviewsByProductId(productId);
    }
}
