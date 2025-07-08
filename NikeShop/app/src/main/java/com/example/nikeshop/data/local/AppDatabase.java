package com.example.nikeshop.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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

}
