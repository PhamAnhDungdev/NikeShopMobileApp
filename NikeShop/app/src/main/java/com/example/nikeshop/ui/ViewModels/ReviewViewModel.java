package com.example.nikeshop.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.data.repositories.ReviewRepository;

import java.util.List;

public class ReviewViewModel extends ViewModel {
    private final ReviewRepository reviewRepository;

    public ReviewViewModel(ReviewRepository reviewRepository) {
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
            if (modelClass.isAssignableFrom(ReviewViewModel.class)) {
                return (T) new ReviewViewModel(reviewRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
