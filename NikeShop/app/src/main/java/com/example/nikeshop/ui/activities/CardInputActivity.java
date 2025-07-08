package com.example.nikeshop.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nikeshop.R;

import java.util.regex.Pattern;

public class CardInputActivity extends AppCompatActivity {

    private ImageView btnBack, ivSaveCardCheck;
    private EditText etCardName, etCardNumber, etExpiryDate, etCvv;
    private LinearLayout layoutSaveCard;
    private Button btnApply;

    private boolean isSaveCardChecked = true;

    // Card validation patterns
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{13,19}$");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/([0-9]{2})$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3,4}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_input);

        initViews();
        setupClickListeners();
        setupTextWatchers();
        loadSavedData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        ivSaveCardCheck = findViewById(R.id.iv_save_card_check);
        etCardName = findViewById(R.id.et_card_name);
        etCardNumber = findViewById(R.id.et_card_number);
        etExpiryDate = findViewById(R.id.et_expiry_date);
        etCvv = findViewById(R.id.et_cvv);
        layoutSaveCard = findViewById(R.id.layout_save_card);
        btnApply = findViewById(R.id.btn_apply);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        layoutSaveCard.setOnClickListener(v -> toggleSaveCard());

        btnApply.setOnClickListener(v -> processPayment());
    }

    private void setupTextWatchers() {
        // Card Number Formatting (Add spaces every 4 digits)
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;
                String input = s.toString().replaceAll("\\s", "");
                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < input.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ");
                    }
                    formatted.append(input.charAt(i));
                }

                etCardNumber.setText(formatted.toString());
                etCardNumber.setSelection(formatted.length());
                isFormatting = false;
            }
        });

        // Expiry Date Formatting (MM/YY)
        etExpiryDate.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;
                String input = s.toString().replaceAll("/", "");

                if (input.length() >= 2) {
                    String month = input.substring(0, 2);
                    String year = input.length() > 2 ? input.substring(2, Math.min(4, input.length())) : "";

                    // Validate month
                    int monthInt = Integer.parseInt(month);
                    if (monthInt > 12) {
                        month = "12";
                    } else if (monthInt == 0) {
                        month = "01";
                    }

                    String formatted = month + (year.length() > 0 ? "/" + year : "");
                    etExpiryDate.setText(formatted);
                    etExpiryDate.setSelection(formatted.length());
                }

                isFormatting = false;
            }
        });

        // Real-time validation
        etCardName.addTextChangedListener(createValidationWatcher());
        etCardNumber.addTextChangedListener(createValidationWatcher());
        etExpiryDate.addTextChangedListener(createValidationWatcher());
        etCvv.addTextChangedListener(createValidationWatcher());
    }

    private TextWatcher createValidationWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        };
    }

    private void toggleSaveCard() {
        isSaveCardChecked = !isSaveCardChecked;
        updateSaveCardUI();
    }

    private void updateSaveCardUI() {
        if (isSaveCardChecked) {
            ivSaveCardCheck.setImageResource(R.drawable.ic_check_circle_filled);
        } else {
            ivSaveCardCheck.setImageResource(R.drawable.ic_check_circle_empty);
        }
    }

    private void validateForm() {
        boolean isValid = true;

        // Validate card name
        String cardName = etCardName.getText().toString().trim();
        if (cardName.length() < 2) {
            isValid = false;
        }

        // Validate card number
        String cardNumber = etCardNumber.getText().toString().replaceAll("\\s", "");
        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            isValid = false;
        }

        // Validate expiry date
        String expiryDate = etExpiryDate.getText().toString();
        if (!EXPIRY_PATTERN.matcher(expiryDate).matches()) {
            isValid = false;
        } else {
            // Check if date is not expired
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt("20" + parts[1]);

            java.util.Calendar now = java.util.Calendar.getInstance();
            int currentYear = now.get(java.util.Calendar.YEAR);
            int currentMonth = now.get(java.util.Calendar.MONTH) + 1;

            if (year < currentYear || (year == currentYear && month < currentMonth)) {
                isValid = false;
            }
        }

        // Validate CVV
        String cvv = etCvv.getText().toString();
        if (!CVV_PATTERN.matcher(cvv).matches()) {
            isValid = false;
        }

        // Update button state
        btnApply.setEnabled(isValid);
        btnApply.setAlpha(isValid ? 1.0f : 0.5f);
    }

    private void processPayment() {
        String cardName = etCardName.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().replaceAll("\\s", "");
        String expiryDate = etExpiryDate.getText().toString();
        String cvv = etCvv.getText().toString();

        // Final validation
        if (!validateCardDetails(cardName, cardNumber, expiryDate, cvv)) {
            return;
        }

        // Save card if requested
        if (isSaveCardChecked) {
            saveCardDetails(cardName, cardNumber, expiryDate);
        }

        // Simulate payment processing
        btnApply.setText("PROCESSING...");
        btnApply.setEnabled(false);

        // Simulate API call delay
        btnApply.postDelayed(() -> {
            Toast.makeText(this, "Payment processed successfully!", Toast.LENGTH_LONG).show();
            finish();
        }, 2000);
    }

    private boolean validateCardDetails(String name, String number, String expiry, String cvv) {
        if (name.length() < 2) {
            etCardName.setError("Please enter a valid name");
            etCardName.requestFocus();
            return false;
        }

        if (!CARD_NUMBER_PATTERN.matcher(number).matches()) {
            etCardNumber.setError("Please enter a valid card number");
            etCardNumber.requestFocus();
            return false;
        }

        if (!EXPIRY_PATTERN.matcher(expiry).matches()) {
            etExpiryDate.setError("Please enter a valid expiry date");
            etExpiryDate.requestFocus();
            return false;
        }

        if (!CVV_PATTERN.matcher(cvv).matches()) {
            etCvv.setError("Please enter a valid CVV");
            etCvv.requestFocus();
            return false;
        }

        return true;
    }

    private void saveCardDetails(String name, String number, String expiry) {
        // Save to SharedPreferences (in real app, use secure storage)
        String maskedNumber = "**** **** **** " + number.substring(number.length() - 4);

        getSharedPreferences("card_prefs", MODE_PRIVATE)
                .edit()
                .putString("card_name", name)
                .putString("card_number_masked", maskedNumber)
                .putString("card_expiry", expiry)
                .putBoolean("card_saved", true)
                .apply();
    }

    private void loadSavedData() {
        // Load saved card data if available
        boolean cardSaved = getSharedPreferences("card_prefs", MODE_PRIVATE)
                .getBoolean("card_saved", false);

        if (cardSaved) {
            String savedName = getSharedPreferences("card_prefs", MODE_PRIVATE)
                    .getString("card_name", "");
            etCardName.setText(savedName);
        }

        updateSaveCardUI();
        validateForm();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}