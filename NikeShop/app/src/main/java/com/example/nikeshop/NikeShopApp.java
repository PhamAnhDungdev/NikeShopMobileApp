package com.example.nikeshop;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.nikeshop.data.local.AppDatabase;

public class NikeShopApp extends Application {

    private static NikeShopApp instance;
    private static AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "nike_shop_db"
        ).fallbackToDestructiveMigration().build();
        // Khởi tạo các thư viện toàn cục ở đây nếu cần
        // Ví dụ:
        // Stetho.initializeWithDefaults(this);
        // FirebaseApp.initializeApp(this);
        // OneSignal.initWithContext(this);
    }

    public static NikeShopApp getInstance() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return database;
    }
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}