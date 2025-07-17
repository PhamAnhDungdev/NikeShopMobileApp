package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.UserDao;
import com.example.nikeshop.data.local.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button resetButton;
    private String email; // được truyền từ AccountRecovery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        resetButton = findViewById(R.id.resetButton);

        // Lấy email từ Intent
        email = getIntent().getStringExtra("email");

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "nike_database").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();

        resetButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in both fields");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showToast("Passwords do not match");
                return;
            }

            User user = userDao.getUserByEmail(email);
            if (user != null) {
                user.setPasswordHash(hashPassword(newPassword));
                userDao.updateUser(user);

                showToast("Password reset successfully!");
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                finish();
            } else {
                showToast("User not found");
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
