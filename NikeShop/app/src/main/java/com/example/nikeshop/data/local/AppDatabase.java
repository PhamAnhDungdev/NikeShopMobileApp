package com.example.nikeshop.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nikeshop.data.DateConverter;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.dao.CategoryDao;
import com.example.nikeshop.data.local.dao.CouponDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.dao.ReviewDao;
import com.example.nikeshop.data.local.dao.UserDao;
import com.example.nikeshop.data.local.dao.WishlistDao;
import com.example.nikeshop.data.local.entity.Cart;
import com.example.nikeshop.data.local.entity.Category;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.User;
import com.example.nikeshop.data.local.entity.Coupon;
import com.example.nikeshop.data.local.entity.Review;
import com.example.nikeshop.data.local.entity.Wishlist;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.OrderDetail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class,
                Product.class,
                Category.class,
                Cart.class,
                Coupon.class,
                Review.class,
                Wishlist.class,
                Order.class,
                OrderDetail.class,
        }, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract CartDao cartDao();
    public abstract CouponDao couponDao();
    public abstract ReviewDao reviewDao();
    public abstract WishlistDao wishlistDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "NikeShopDatabase"
                            ).build();
                }
            }
        }
        return INSTANCE;
    }
    public static String hashPassword(String password) {
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
