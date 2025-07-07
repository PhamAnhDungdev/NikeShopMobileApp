package com.example.nikeshop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

public class ContactInfoActivity extends AppCompatActivity {

    private ImageButton btnBack, btnSave;
    private EditText etFirstName, etLastName, etEmail, etMobile;

    private boolean isDataChanged = false;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Phone validation pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

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
        // Load saved contact info from SharedPreferences
        String firstName = getSharedPreferences("contact_prefs", MODE_PRIVATE)
                .getString("first_name", "Aaron");
        String lastName = getSharedPreferences("contact_prefs", MODE_PRIVATE)
                .getString("last_name", "Paul");
        String email = getSharedPreferences("contact_prefs", MODE_PRIVATE)
                .getString("email", "Pravy@gmail.com");
        String mobile = getSharedPreferences("contact_prefs", MODE_PRIVATE)
                .getString("mobile", "8828828828");

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etEmail.setText(email);
        etMobile.setText(mobile);

        isDataChanged = false;
        validateForm();
    }

    private void validateForm() {
        boolean isValid = true;

        // Validate first name
        String firstName = etFirstName.getText().toString().trim();
        if (firstName.length() < 2) {
            isValid = false;
        }

        // Validate last name
        String lastName = etLastName.getText().toString().trim();
        if (lastName.length() < 2) {
            isValid = false;
        }

        // Validate email
        String email = etEmail.getText().toString().trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            isValid = false;
        }

        // Validate mobile
        String mobile = etMobile.getText().toString().trim();
        if (!PHONE_PATTERN.matcher(mobile).matches()) {
            isValid = false;
        }

        // Update save button state
        btnSave.setEnabled(isValid && isDataChanged);
        btnSave.setAlpha(isValid && isDataChanged ? 1.0f : 0.5f);
    }

    private void saveContactInfo() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        // Final validation
        if (!validateContactDetails(firstName, lastName, email, mobile)) {
            return;
        }

        // Save to SharedPreferences
        getSharedPreferences("contact_prefs", MODE_PRIVATE)
                .edit()
                .putString("first_name", firstName)
                .putString("last_name", lastName)
                .putString("email", email)
                .putString("mobile", mobile)
                .apply();

        isDataChanged = false;
        Toast.makeText(this, "Contact info saved successfully!", Toast.LENGTH_SHORT).show();

        // Optionally finish activity
        finish();
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