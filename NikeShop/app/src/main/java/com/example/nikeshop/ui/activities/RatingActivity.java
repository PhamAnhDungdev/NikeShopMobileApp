package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.ui.ViewModels.ReviewViewModel;

public class RatingActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvRatingTitle;
    private RatingBar ratingBar;
    private EditText etReview;
    private Button btnSubmitRating;
    private ProgressBar progressBar;

    private ReviewViewModel reviewViewModel;
    private int orderDetailId;
    private int productId;
    private int orderId;
    private String productName;
    private int currentUserId; // TODO: Get from user session/preferences
    private Review existingReview;
    private String productImageUrl; // add this field


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        getIntentData();
        initViews();
        setupViewModel();
        setupClickListeners();
        displayProductInfo();
        checkExistingReview();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        orderDetailId = intent.getIntExtra("ORDER_DETAIL_ID", -1);
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        orderId = intent.getIntExtra("ORDER_ID", -1);
        productName = intent.getStringExtra("PRODUCT_NAME");
        productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL");

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.getBoolean("is_logged_in", false)) {
            startActivity(new Intent(RatingActivity.this, LoginActivity.class));
            finish();
            return;
        }

         currentUserId = getSharedPreferences("user_session", MODE_PRIVATE)
                .getInt("user_id", -1);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvRatingTitle = findViewById(R.id.tv_rating_title);
        ratingBar = findViewById(R.id.rating_bar);
        etReview = findViewById(R.id.et_review);
        btnSubmitRating = findViewById(R.id.btn_submit_rating);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupViewModel() {
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        // Observe loading state
        reviewViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSubmitRating.setEnabled(!isLoading);
        });

        // Observe error messages
        reviewViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Observe review submission success
        reviewViewModel.getReviewSubmitted().observe(this, submitted -> {
            if (submitted) {
                Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Observe existing review
        reviewViewModel.getExistingReview().observe(this, review -> {
            existingReview = review;
            if (review != null) {
                // User has already reviewed this product
                tvRatingTitle.setText("Update your review");
                btnSubmitRating.setText("Update Review");
                ratingBar.setRating(review.getRating());
                etReview.setText(review.getComment());
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnSubmitRating.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = etReview.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingReview != null) {
                // Update existing review
                reviewViewModel.updateReview(existingReview, (int) rating, comment);
            } else {
                // Submit new review
                reviewViewModel.submitReview(currentUserId, productId, (int) rating, comment);
            }
        });
    }

    private void displayProductInfo() {
        if (productName != null) {
            tvProductName.setText(productName);
        }
        if (productImageUrl != null && !productImageUrl.isEmpty()) {
            Glide.with(this).load(productImageUrl).into(ivProductImage); // ✅ hiển thị ảnh
        }
    }

    private void checkExistingReview() {
        reviewViewModel.checkExistingReview(currentUserId, productId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
