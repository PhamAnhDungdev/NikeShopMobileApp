package com.example.nikeshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_help extends AppCompatActivity {

    private ImageView btnBack;;
    private LinearLayout optionUpdateAccount;
    private LinearLayout optionChangePassword;
    private LinearLayout optionUnsubscribe;
    private LinearLayout optionSignUpMobile;
    private LinearLayout optionPasswordCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        optionUpdateAccount = findViewById(R.id.optionUpdateAccount);
        optionChangePassword = findViewById(R.id.optionChangePassword);
        optionUnsubscribe = findViewById(R.id.optionUnsubscribe);
        optionSignUpMobile = findViewById(R.id.optionSignUpMobile);
        optionPasswordCode = findViewById(R.id.optionPasswordCode);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        optionUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdateAccount();
            }
        });

        optionChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });

        optionUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUnsubscribe();
            }
        });

        optionSignUpMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUpMobile();
            }
        });

        optionPasswordCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePasswordCode();
            }
        });
    }

    private void handleUpdateAccount() {
        // Navigate to account update screen
        Toast.makeText(this, "Opening account update...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, UpdateAccountActivity.class);
        // startActivity(intent);
    }

    private void handleChangePassword() {
        // Navigate to password change screen
        Toast.makeText(this, "Opening password change...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, ChangePasswordActivity.class);
        // startActivity(intent);
    }

    private void handleUnsubscribe() {
        // Navigate to unsubscribe screen
        Toast.makeText(this, "Opening newsletter settings...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, UnsubscribeActivity.class);
        // startActivity(intent);
    }

    private void handleSignUpMobile() {
        // Navigate to mobile signup screen
        Toast.makeText(this, "Opening mobile signup...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, MobileSignupActivity.class);
        // startActivity(intent);
    }

    private void handlePasswordCode() {
        // Navigate to password verification screen
        Toast.makeText(this, "Opening verification help...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, PasswordVerificationActivity.class);
        // startActivity(intent);
    }
}