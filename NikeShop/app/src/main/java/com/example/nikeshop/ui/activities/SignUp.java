package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.room.Room;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.UserDao;
import com.example.nikeshop.data.local.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        EditText nameInput = findViewById(R.id.nameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        Button signUpButton = findViewById(R.id.signUpButton);
        ImageView eyeIconPassword = findViewById(R.id.eyeIconPassword);
        ImageView eyeIconConfirmPassword = findViewById(R.id.eyeIconConfirmPassword);
        TextView signInLink = findViewById(R.id.signInLink);

        // DB
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "nike_shop_db").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();

        // Icon hiện/ẩn mật khẩu
        setupEyeToggle(eyeIconPassword, passwordInput);
        setupEyeToggle(eyeIconConfirmPassword, confirmPasswordInput);

        // Chuyển về Login
        signInLink.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, LoginActivity.class));
            finish();
        });

        // Nút Đăng ký
        signUpButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in all fields");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Please enter a valid email address");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showToast("Passwords do not match");
                return;
            }

            if (userDao.getUserByEmail(email) != null) {
                showToast("Email is already registered");
                return;
            }

            // Lưu user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPasswordHash(hashPassword(password));

            userDao.insertUser(user);

            showToast("Account created successfully!");
            startActivity(new Intent(SignUp.this, LoginActivity.class));
            finish();
        });
    }

    private void setupEyeToggle(ImageView eyeIcon, EditText passwordField) {
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            boolean isVisible = false;
            @Override
            public void onClick(View v) {
                isVisible = !isVisible;
                if (isVisible) {
                    passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordField.setSelection(passwordField.length());
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
            return null;
        }
    }
}
