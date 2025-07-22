package com.example.nikeshop.data.local.seed;

import android.content.Context;
import android.util.Log;
import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class Seeder {
    private static final boolean FORCE_RESEED = false; // False khong reset // True reset

    // Custom Date Deserializer
    private static class DateDeserializer implements JsonDeserializer<Date> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }
            try {
                return dateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static void seed(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream inputStream = context.getAssets().open("seed_data.json");
                InputStreamReader reader = new InputStreamReader(inputStream);


                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .create();

                SeedData seedData = gson.fromJson(reader, SeedData.class);
                AppDatabase db = NikeShopApp.getDatabase();

                boolean isDatabaseEmpty =
                        db.categoryDao().countCategories() == 0 &&
                                db.userDao().countUsers() == 0 &&
                                db.productDao().countProducts() == 0 &&
                                db.orderDao().countOrders() == 0 &&
                                db.orderDetailDao().countOrderDetails() == 0 &&
                                db.cartDao().countCarts() == 0 &&
                                db.reviewDao().countReviews() == 0 &&
                                db.wishlistDao().countWishlists() == 0 &&
                                db.couponDao().countCoupons() == 0;
                if (!isDatabaseEmpty && !FORCE_RESEED) {
                    Log.d("Seeder", "Skipping seeding. Database already has data.");
                    return;
                }

                if (FORCE_RESEED) {
                    db.clearAllTables();
                    Log.d("Seeder", "Forced reseed: cleared all tables.");
                }

                Log.d("Seeder", "Starting database seeding...");


                    // 1. Insert categories first
                    List<Long> categoryIds = db.categoryDao().insertAll(seedData.categories);
                    Log.d("Seeder", "Inserted " + categoryIds.size() + " categories");

                    // 2. Insert users
                    List<Long> userIds = db.userDao().insertAll(seedData.users);
                    Log.d("Seeder", "Inserted " + userIds.size() + " users");

                    // 3. Update product category references and insert products
                    for (int i = 0; i < seedData.products.size(); i++) {
                        Product product = seedData.products.get(i);
                        // Map old category ID to new auto-generated ID
                        if (product.getCategoryId() > 0 && product.getCategoryId() <= categoryIds.size()) {
                            product.setCategoryId(categoryIds.get(product.getCategoryId() - 1).intValue());
                        }
                    }
                    List<Long> productIds = db.productDao().insertAll(seedData.products);
                    Log.d("Seeder", "Inserted " + productIds.size() + " products");

                    // 4. Update order user references and insert orders
                    for (int i = 0; i < seedData.orders.size(); i++) {
                        Order order = seedData.orders.get(i);
                        // Map old user ID to new auto-generated ID
                        if (order.getUserId() > 0 && order.getUserId() <= userIds.size()) {
                            order.setUserId(userIds.get(order.getUserId() - 1).intValue());
                        }
                    }
                    List<Long> orderIds = db.orderDao().insertAll(seedData.orders);
                    Log.d("Seeder", "Inserted " + orderIds.size() + " orders");

                    // 5. Update order detail references and insert
                    for (int i = 0; i < seedData.orderDetails.size(); i++) {
                        OrderDetail detail = seedData.orderDetails.get(i);
                        // Map old order ID to new auto-generated ID
                        if (detail.getOrderId() > 0 && detail.getOrderId() <= orderIds.size()) {
                            detail.setOrderId(orderIds.get(detail.getOrderId() - 1).intValue());
                        }
                        // Map old product ID to new auto-generated ID
                        if (detail.getProductId() > 0 && detail.getProductId() <= productIds.size()) {
                            detail.setProductId(productIds.get(detail.getProductId() - 1).intValue());
                        }
                    }
                    List<Long> orderDetailIds = db.orderDetailDao().insertAll(seedData.orderDetails);
                    Log.d("Seeder", "Inserted " + orderDetailIds.size() + " order details");

                    // 6. Update cart references and insert
                    for (int i = 0; i < seedData.carts.size(); i++) {
                        Cart cart = seedData.carts.get(i);
                        // Map old user ID to new auto-generated ID
                        if (cart.getUserId() > 0 && cart.getUserId() <= userIds.size()) {
                            cart.setUserId(userIds.get(cart.getUserId() - 1).intValue());
                        }
                        // Map old product ID to new auto-generated ID
                        if (cart.getProductId() > 0 && cart.getProductId() <= productIds.size()) {
                            cart.setProductId(productIds.get(cart.getProductId() - 1).intValue());
                        }
                    }
                    List<Long> cartIds = db.cartDao().insertAll(seedData.carts);
                    Log.d("Seeder", "Inserted " + cartIds.size() + " cart items");

                    // 7. Update review references and insert
                    for (int i = 0; i < seedData.reviews.size(); i++) {
                        Review review = seedData.reviews.get(i);
                        // Map old user ID to new auto-generated ID
                        if (review.getUserId() > 0 && review.getUserId() <= userIds.size()) {
                            review.setUserId(userIds.get(review.getUserId() - 1).intValue());
                        }
                        // Map old product ID to new auto-generated ID
                        if (review.getProductId() > 0 && review.getProductId() <= productIds.size()) {
                            review.setProductId(productIds.get(review.getProductId() - 1).intValue());
                        }
                    }
                    List<Long> reviewIds = db.reviewDao().insertAll(seedData.reviews);
                    Log.d("Seeder", "Inserted " + reviewIds.size() + " reviews");

                    // 8. Update wishlist references and insert
                    for (int i = 0; i < seedData.wishlists.size(); i++) {
                        Wishlist wishlist = seedData.wishlists.get(i);
                        // Map old user ID to new auto-generated ID
                        if (wishlist.getUserId() > 0 && wishlist.getUserId() <= userIds.size()) {
                            wishlist.setUserId(userIds.get(wishlist.getUserId() - 1).intValue());
                        }
                        // Map old product ID to new auto-generated ID
                        if (wishlist.getProductId() > 0 && wishlist.getProductId() <= productIds.size()) {
                            wishlist.setProductId(productIds.get(wishlist.getProductId() - 1).intValue());
                        }
                    }
                    List<Long> wishlistIds = db.wishlistDao().insertAll(seedData.wishlists);
                    Log.d("Seeder", "Inserted " + wishlistIds.size() + " wishlist items");

                    // 9. Insert coupons (no foreign key dependencies)
                    List<Long> couponIds = db.couponDao().insertAll(seedData.coupons);
                    Log.d("Seeder", "Inserted " + couponIds.size() + " coupons");

                    Log.d("Seeder", "Database seeded successfully!");
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
