package com.example.nikeshop.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.data.repositories.ReviewRepository;

import java.util.List;

public class ReviewViewModelProductDetail extends ViewModel {
    private final ReviewRepository reviewRepository;

    public ReviewViewModelProductDetail(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LiveData<List<Review>> getReviewsByProductId(int productId) {
        return reviewRepository.getReviewsByProductId(productId);
    }

    // Factory for ViewModel with constructor
    public static class Factory implements ViewModelProvider.Factory {
        private final ReviewRepository reviewRepository;

        public Factory(ReviewRepository reviewRepository) {
            this.reviewRepository = reviewRepository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ReviewViewModelProductDetail.class)) {
                return (T) new ReviewViewModelProductDetail(reviewRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}