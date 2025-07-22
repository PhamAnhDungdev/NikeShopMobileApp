package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.User;
import com.example.nikeshop.ui.ViewModels.UserViewModel;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ContactInfoActivity extends AppCompatActivity {
    private ImageButton btnBack, btnSave;
    private EditText etFirstName, etLastName, etEmail, etMobile;
    private boolean isDataChanged = false;
    private UserViewModel userViewModel;
    private User currentUser; // Thêm biến để lưu user hiện tại

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initViews();
        setupClickListeners();
        setupTextWatchers();
        loadContactInfo();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etMobile = findViewById(R.id.et_mobile);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataChanged) {
                    showUnsavedChangesDialog();
                } else {
                    onBackPressed();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactInfo();
            }
        });
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDataChanged = true;
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etMobile.addTextChangedListener(textWatcher);
    }

    private void loadContactInfo() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.getBoolean("is_logged_in", false)) {
            startActivity(new Intent(ContactInfoActivity.this, LoginActivity.class));
            finish();
            return;
        }

        int userId = getSharedPreferences("user_session", MODE_PRIVATE)
                .getInt("user_id", -1);

        userViewModel.getUserById(userId).observe(this, user -> {
            if (user != null) {
                currentUser = user; // Lưu user hiện tại
                etFirstName.setText(user.getUsername());
                etLastName.setText(""); // Nếu có trường lastName trong User entity thì dùng user.getLastName()
                etEmail.setText(user.getEmail());
                etMobile.setText(user.getPhone());
                isDataChanged = false;
                validateForm();
            } else {
                Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateForm() {
        boolean isValid = true;

        String firstName = etFirstName.getText().toString().trim();
        if (firstName.length() < 2) {
            isValid = false;
        }

        String lastName = etLastName.getText().toString().trim();
        if (lastName.length() < 2) {
            isValid = false;
        }

        String email = etEmail.getText().toString().trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            isValid = false;
        }

        String mobile = etMobile.getText().toString().trim();
        if (!PHONE_PATTERN.matcher(mobile).matches()) {
            isValid = false;
        }

        btnSave.setEnabled(isValid && isDataChanged);
        btnSave.setAlpha(isValid && isDataChanged ? 1.0f : 0.5f);
    }

    private void saveContactInfo() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        if (!validateContactDetails(firstName, lastName, email, mobile)) {
            return;
        }

        if (currentUser != null) {
            // Cập nhật thông tin user
            currentUser.setUsername(firstName + lastName); // Hoặc setFirstName nếu có
            currentUser.setEmail(email);
            currentUser.setPhone(mobile);
            // Nếu có trường lastName thì: currentUser.setLastName(lastName);

            // Cập nhật vào database
            Executors.newSingleThreadExecutor().execute(() -> {
                userViewModel.update(currentUser);

                runOnUiThread(() -> {
                    getSharedPreferences("user_session", MODE_PRIVATE)
                            .edit()
                            .putBoolean("is_logged_in", true)
                            .putInt("user_id", currentUser.getId())
                            .putString("user_name", currentUser.getUsername())
                            .putString("user_email", currentUser.getEmail())
                            .putBoolean("is_admin", currentUser.isAdmin())
                            .apply();

                    isDataChanged = false;
                    Toast.makeText(this, "Contact info updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        } else {
            Toast.makeText(this, "Error: User data not loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateContactDetails(String firstName, String lastName, String email, String mobile) {
        if (firstName.length() < 2) {
            etFirstName.setError("Please enter a valid first name");
            etFirstName.requestFocus();
            return false;
        }

        if (lastName.length() < 2) {
            etLastName.setError("Please enter a valid last name");
            etLastName.requestFocus();
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        if (!PHONE_PATTERN.matcher(mobile).matches()) {
            etMobile.setError("Please enter a valid mobile number");
            etMobile.requestFocus();
            return false;
        }

        return true;
    }

    private void showUnsavedChangesDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you want to save them?")
                .setPositiveButton("Save", (dialog, which) -> {
                    saveContactInfo();
                })
                .setNegativeButton("Discard", (dialog, which) -> {
                    finish();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (isDataChanged) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
