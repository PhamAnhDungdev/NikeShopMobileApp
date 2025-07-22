package com.example.nikeshop.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nikeshop.R;

public class ProfileActivity extends BottomMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View topNavBar = findViewById(R.id.top_navbar);
        View btnBack = topNavBar.findViewById(R.id.btn_back);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        if (!prefs.getBoolean("is_logged_in", false)) {
            // Nếu chưa đăng nhập thì chuyển về LoginActivity
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String userName = prefs.getString("user_name", "Guest");
        String mail = prefs.getString("user_email", "No contact");
        TextView name = findViewById(R.id.tvName);
        TextView email = findViewById(R.id.tvLocation);
        name.setText(userName);
        email.setText(mail);

        // ✅ Setup bottom menu
        setupBottomMenu(R.id.nav_profile);

        // ✅ Các sự kiện click
        findViewById(R.id.btnEditProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ContactInfoActivity.class)));

        findViewById(R.id.llMyOrders).setOnClickListener(v ->
                startActivity(new Intent(this, MyOrdersActivity.class)));

        findViewById(R.id.llMyCards).setOnClickListener(v ->
                startActivity(new Intent(this, MyCardsActivity.class)));

        findViewById(R.id.llAddress).setOnClickListener(v ->
                startActivity(new Intent(this, AddressActivity.class)));

        findViewById(R.id.llHelpCenter).setOnClickListener(v ->
                startActivity(new Intent(this, HelpActivity.class)));

        findViewById(R.id.llLogout).setOnClickListener(v -> {
            // Xóa session
            prefs.edit().clear().apply();
            // Chuyển về LoginActivity và xóa stack
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
