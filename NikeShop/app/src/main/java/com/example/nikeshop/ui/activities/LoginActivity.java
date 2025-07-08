package com.example.nikeshop.ui.activities;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.NotificationManager;

import com.example.nikeshop.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Thêm sự kiện chuyển hướng sang SignUp khi click vào signUpLink
        TextView signUpLink = findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        // Thêm sự kiện chuyển hướng sang AccountRecovery khi click vào forgotPasswordLink
        TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AccountRecovery.class);
                startActivity(intent);
            }
        });
        sendTestNotification();

        // Thêm sự kiện chuyển hướng sang ActivityHome khi click vào skipNow
        TextView skipNow = findViewById(R.id.skipNow);
        skipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendTestNotification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        Notification notification = new NotificationCompat.Builder(this, "login_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // dùng icon mặc định hoặc thay icon app bạn
                .setContentTitle("Welcome to NikeShop")
                .setContentText("This is a test notification.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }

    }

}