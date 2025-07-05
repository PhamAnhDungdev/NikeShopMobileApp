package com.example.nikeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MyCardsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RelativeLayout creditCard;
    private LinearLayout btnAddCard;
    private LinearLayout navHome, navSearch, navFavourites, navProfile;
    private TextView tvCardNumber, tvCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);

        initViews();
        setupClickListeners();
        loadCardData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        creditCard = findViewById(R.id.credit_card);
        btnAddCard = findViewById(R.id.btn_add_card);
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navFavourites = findViewById(R.id.nav_favourites);
        navProfile = findViewById(R.id.nav_profile);
        tvCardNumber = findViewById(R.id.tv_card_number);
        tvCardType = findViewById(R.id.tv_card_type);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDetails();
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCardScreen();
            }
        });

        // Navigation click listeners
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearch();
            }
        });

        navFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFavourites();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on profile section
                Toast.makeText(MyCardsActivity.this, "You're in Profile section", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCardData() {
        // Load saved card data from SharedPreferences
        boolean cardSaved = getSharedPreferences("card_prefs", MODE_PRIVATE)
                .getBoolean("card_saved", false);

        if (cardSaved) {
            String savedName = getSharedPreferences("card_prefs", MODE_PRIVATE)
                    .getString("card_name", "A. MILLER");
            String maskedNumber = getSharedPreferences("card_prefs", MODE_PRIVATE)
                    .getString("card_number_masked", "4000 1234 5678 9010");
            String expiry = getSharedPreferences("card_prefs", MODE_PRIVATE)
                    .getString("card_expiry", "12/20");

            // Update card display with saved data
            tvCardNumber.setText(maskedNumber);
            // You can update other fields as needed
        }
    }

    private void showCardDetails() {
        Toast.makeText(this, "Card details", Toast.LENGTH_SHORT).show();
        // You can open a detailed card view or card management screen
    }

    private void openAddCardScreen() {
        // Navigate to CardInputActivity
        Intent intent = new Intent(this, CardInputActivity.class);
        startActivity(intent);
    }

    private void navigateToHome() {
        Toast.makeText(this, "Navigate to Home", Toast.LENGTH_SHORT).show();
        // Intent to HomeActivity
    }

    private void navigateToSearch() {
        Toast.makeText(this, "Navigate to Search", Toast.LENGTH_SHORT).show();
        // Intent to SearchActivity
    }

    private void navigateToFavourites() {
        Toast.makeText(this, "Navigate to Favourites", Toast.LENGTH_SHORT).show();
        // Intent to FavouritesActivity
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload card data when returning from add card screen
        loadCardData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}