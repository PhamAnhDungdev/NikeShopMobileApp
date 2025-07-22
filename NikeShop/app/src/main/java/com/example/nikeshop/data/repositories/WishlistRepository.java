package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.WishlistDao;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.Wishlist;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishlistRepository {

    private final WishlistDao wishlistDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public WishlistRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.wishlistDao = db.wishlistDao();
    }

    public LiveData<List<Wishlist>> getWishlistByUserId(int userId) {
        MutableLiveData<List<Wishlist>> result = new MutableLiveData<>();
        executor.execute(() -> {
            List<Wishlist> list = wishlistDao.getWishlistByUserId(userId);
            result.postValue(list);
        });
        return result;
    }

    public void addToWishlist(Wishlist wishlist) {
        executor.execute(() -> wishlistDao.insert(wishlist));
    }

    public void removeFromWishlist(int userId, int productId) {
        executor.execute(() -> wishlistDao.deleteByUserAndProduct(userId, productId));
    }

    public void clearWishlist(int userId) {
        executor.execute(() -> wishlistDao.deleteAllByUser(userId));
    }

    public LiveData<Integer> countWishlist() {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        executor.execute(() -> result.postValue(wishlistDao.countWishlists()));
        return result;
    }

    public LiveData<List<Product>> getWishlistProductsByUserId(int userId) {
        return wishlistDao.getProductsInWishlistByUserId(userId);
    }

    public LiveData<List<Wishlist>> getAllWishlist() {
        return wishlistDao.getAllWishlistItems();
    }
}
