package com.example.nikeshop.data.local.seed;

import android.content.Context;
import android.util.Log;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executors;

public class Seeder {

    public static void seed(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream inputStream = context.getAssets().open("seed_data.json");
                InputStreamReader reader = new InputStreamReader(inputStream);

                Gson gson = new Gson();
                SeedData seedData = gson.fromJson(reader, SeedData.class);

                AppDatabase db = NikeShopApp.getDatabase();

                db.categoryDao().insertAll(seedData.categories);
                db.userDao().insertAll(seedData.users);
                db.productDao().insertAll(seedData.products);
                db.orderDao().insertAll(seedData.orders);
                db.orderDetailDao().insertAll(seedData.orderDetails);
                db.cartDao().insertAll(seedData.carts);
                db.reviewDao().insertAll(seedData.reviews);
                db.wishlistDao().insertAll(seedData.wishlists);
                db.couponDao().insertAll(seedData.coupons);

                Log.d("Seeder", "Database seeded successfully");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Seeder", "Seeding failed: " + e.getMessage());
            }
        });
    }

    // Wrapper class để chứa danh sách các bảng
    static class SeedData {
        List<Category> categories;
        List<User> users;
        List<Product> products;
        List<Order> orders;
        List<OrderDetail> orderDetails;
        List<Cart> carts;
        List<Review> reviews;
        List<Wishlist> wishlists;
        List<Coupon> coupons;
    }
}
