package com.example.nikeshop.ui.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.ReviewDao;
import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.data.repositories.ReviewRepository;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewViewModel extends AndroidViewModel {
    private final ReviewRepository reviewRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> reviewSubmitted = new MutableLiveData<>(false);
    private final MutableLiveData<Review> existingReview = new MutableLiveData<>();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        ReviewDao reviewDao = database.reviewDao();
        reviewRepository = new ReviewRepository(reviewDao);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getReviewSubmitted() {
        return reviewSubmitted;
    }

    public LiveData<Review> getExistingReview() {
        return existingReview;
    }

    public LiveData<List<Review>> getReviewsByProduct(int productId) {
        return reviewRepository.getReviewsByProduct(productId);
    }

    public void checkExistingReview(int userId, int productId) {
        executor.execute(() -> {
            Review review = reviewRepository.getReviewByUserAndProduct(userId, productId);
            existingReview.postValue(review);
        });
    }

    public void submitReview(int userId, int productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            errorMessage.postValue("Rating must be between 1 and 5");
            return;
        }

        isLoading.postValue(true);

        Date currentDate = new Date();
        Review review = new Review(
                userId,
                productId,
                rating,
                comment,
                currentDate,
                currentDate,
                currentDate,
                null
        );

        reviewRepository.insertReview(review, new ReviewRepository.OnReviewInsertedListener() {
            @Override
            public void onSuccess(Long reviewId) {
                isLoading.postValue(false);
                reviewSubmitted.postValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Failed to submit review: " + error);
            }
        });
    }

    public void updateReview(Review review, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            errorMessage.postValue("Rating must be between 1 and 5");
            return;
        }

        isLoading.postValue(true);

        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(new Date());

        reviewRepository.updateReview(review, new ReviewRepository.OnReviewUpdatedListener() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                reviewSubmitted.postValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Failed to update review: " + error);
            }
        });
    }

    public void getProductRatingInfo(int productId, OnRatingInfoListener listener) {
        executor.execute(() -> {
            Double averageRating = reviewRepository.getAverageRatingForProduct(productId);
            int reviewCount = reviewRepository.getReviewCountForProduct(productId);

            if (listener != null) {
                listener.onRatingInfo(
                        averageRating != null ? averageRating : 0.0,
                        reviewCount
                );
            }
        });
    }

    public interface OnRatingInfoListener {
        void onRatingInfo(double averageRating, int reviewCount);
    }
}