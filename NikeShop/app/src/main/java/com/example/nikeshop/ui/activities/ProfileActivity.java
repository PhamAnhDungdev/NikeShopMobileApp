package com.example.nikeshop.ui.activities;

import android.os.Bundle;

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
        setupBottomMenu(R.id.nav_profile);

        // Bắt sự kiện click cho các mục
        findViewById(R.id.btnEditProfile).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, ContactInfoActivity.class)));
        findViewById(R.id.llMyOrders).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, MyOrdersActivity.class)));
        findViewById(R.id.llMyCards).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, MyCardsActivity.class)));
        findViewById(R.id.llAddress).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, AddressActivity.class)));
        findViewById(R.id.llHelpCenter).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, HelpActivity.class)));
        findViewById(R.id.llLogout).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}