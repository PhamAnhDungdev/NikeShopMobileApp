package com.example.nikeshop.data.repositories;

import android.app.Application;
import android.os.AsyncTask;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.WishlistDao;
import com.example.nikeshop.data.local.entity.Wishlist;
import java.util.Date;

public class WishlistRepository {
    private final WishlistDao wishlistDao;

    public interface AddToWishlistCallback {
        void onSuccess();
        void onExists();
    }

    public WishlistRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        wishlistDao = db.wishlistDao();
    }

    public void addToWishlist(int productId, int userId, AddToWishlistCallback callback) {
        AsyncTask.execute(() -> {
            int exists = wishlistDao.exists(userId, productId);
            if (exists == 0) {
                Wishlist wishlist = new Wishlist();
                wishlist.setUserId(userId);
                wishlist.setProductId(productId);
                wishlist.setCreatedAt(new Date());
                wishlistDao.insert(wishlist);
                if (callback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(callback::onSuccess);
                }
            } else {
                if (callback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(callback::onExists);
                }
            }
        });
    }
}
