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
            // Find the LinearLayout to show comments (replace with RecyclerView for real app)
            LinearLayout commentsLayout = findViewById(R.id.comments_layout);
            if (commentsLayout != null) {
                // Setup ViewModel
                ReviewDao reviewDao = com.example.nikeshop.data.local.AppDatabase.getInstance(getApplicationContext()).reviewDao();
                // Debug: In ra toàn bộ review trong DB
                new Thread(() -> {
                    List<Review> allReviews = reviewDao.getReviewsListByProductId(0); // Lấy tất cả review, productId=0 sẽ trả về rỗng, cần hàm lấy toàn bộ
                    // Nếu chưa có hàm, hãy tạo hàm getAllReviews() trong ReviewDao
                    // List<Review> allReviews = reviewDao.getAllReviews();
                    android.util.Log.d("ReviewDebug", "--- All reviews in DB ---");
                    for (Review review : allReviews) {
                        android.util.Log.d("ReviewDebug", review.toString());
                    }
                }).start();
                ReviewRepository reviewRepository = new ReviewRepository(reviewDao);
                ReviewViewModel reviewViewModel = new ViewModelProvider(this, new ReviewViewModel.Factory(reviewRepository)).get(ReviewViewModel.class);
                reviewViewModel.getReviewsByProductId(productId).observe(this, reviews -> {
                    commentsLayout.removeAllViews();
                    if (reviews != null && !reviews.isEmpty()) {
                        for (Review review : reviews) {
                            TextView tv = new TextView(this);
                            tv.setText("- " + review.getComment());
                            tv.setTextSize(16);
                            tv.setTextColor(0xFF444444);
                            tv.setPadding(0, 8, 0, 8);
                            commentsLayout.addView(tv);
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