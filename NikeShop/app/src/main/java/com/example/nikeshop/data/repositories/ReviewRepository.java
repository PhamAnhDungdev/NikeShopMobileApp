package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.dao.ReviewDao;
import com.example.nikeshop.data.local.entity.Review;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewRepository {
    private final ReviewDao reviewDao;
    private final ExecutorService executorService;

    public ReviewRepository(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Review>> getReviewsByProductId(int productId) {
        return reviewDao.getReviewsByProductId(productId);
    }

    // You can add insert/delete methods here if needed
}
