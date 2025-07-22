package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.dao.ReviewDao;
import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.data.repositories.ReviewRepository;
import com.example.nikeshop.ui.viewmodel.ReviewViewModel;

import java.util.List;

public class FeedbackActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBottomMenu(R.id.nav_home);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            // Hiển thị tên sản phẩm (background thread)
            TextView tvProductName = findViewById(R.id.tv_product_name);
            new Thread(() -> {
                com.example.nikeshop.data.local.entity.Product product = com.example.nikeshop.data.local.AppDatabase.getInstance(getApplicationContext()).productDao().getProductById(productId);
                String productName = (product != null && product.getName() != null) ? product.getName() : "";
                runOnUiThread(() -> tvProductName.setText(productName));
            }).start();
            // Find the LinearLayout to show comments (replace with RecyclerView for real app)
            LinearLayout commentsLayout = findViewById(R.id.comments_layout);
            if (commentsLayout != null) {
                // Setup ViewModel
                ReviewDao reviewDao = com.example.nikeshop.data.local.AppDatabase.getInstance(getApplicationContext()).reviewDao();
                ReviewRepository reviewRepository = new ReviewRepository(reviewDao);
                ReviewViewModel reviewViewModel = new ViewModelProvider(this, new ReviewViewModel.Factory(reviewRepository)).get(ReviewViewModel.class);
                reviewViewModel.getReviewsByProductId(productId).observe(this, reviews -> {
                    commentsLayout.removeAllViews();
                    if (reviews != null && !reviews.isEmpty()) {
                        for (Review review : reviews) {
                            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
                            android.view.View reviewView = inflater.inflate(R.layout.item_review, commentsLayout, false);
                            // Username (background thread)
                            TextView tvUsername = reviewView.findViewById(R.id.tv_username);
                            int userId = review.getUserId();
                            new Thread(() -> {
                                String tempUsername = "Người dùng";
                                com.example.nikeshop.data.local.entity.User user = com.example.nikeshop.data.local.AppDatabase.getInstance(getApplicationContext()).userDao().getUserById(userId);
                                if (user != null && user.getUsername() != null) tempUsername = user.getUsername();
                                final String finalUsername = tempUsername;
                                runOnUiThread(() -> tvUsername.setText(finalUsername));
                            }).start();
                            // Review date
                            TextView tvDate = reviewView.findViewById(R.id.tv_review_date);
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            String dateStr = sdf.format(review.getReviewDate());
                            tvDate.setText(dateStr);
                            // Rating
                            LinearLayout ratingLayout = reviewView.findViewById(R.id.rating_layout);
                            if (ratingLayout != null) {
                                ratingLayout.removeAllViews();
                                int rating = review.getRating();
                                int fullStars = rating;
                                int halfStar = 0;
                                // Nếu rating là số thập phân, ví dụ 4.5 thì fullStars=4, halfStar=1
                                if (rating < 5 && rating > 0 && (rating * 10) % 10 >= 5) {

                                    fullStars = rating;
                                    halfStar = 1;
                                }
                                for (int i = 1; i <= 5; i++) {
                                    android.widget.ImageView star = new android.widget.ImageView(this);
                                    if (i <= fullStars) {
                                        star.setImageResource(R.drawable.ic_star_filled);
                                    } else if (halfStar == 1 && i == fullStars + 1) {
                                        star.setImageResource(R.drawable.ic_star_half);
                                    } else {
                                        star.setImageResource(R.drawable.ic_star_outline);
                                    }
                                    star.setLayoutParams(new LinearLayout.LayoutParams(32, 32));
                                    ratingLayout.addView(star);
                                }
                            }
                            // Comment
                            TextView tvComment = reviewView.findViewById(R.id.tv_comment);
                            tvComment.setText(review.getComment());
                            commentsLayout.addView(reviewView);
                        }
                    } else {
                        TextView tv = new TextView(this);
                        tv.setText("Chưa có bình luận nào cho sản phẩm này.");
                        tv.setTextSize(16);
                        tv.setTextColor(0xFF888888);
                        commentsLayout.addView(tv);
                    }
                });
            }
        }
    }
}