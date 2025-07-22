package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.nikeshop.data.repositories.WishlistRepository;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.Wishlist;
import com.example.nikeshop.data.repositories.WishlistRepository;

import java.util.List;

public class WishlistViewModel extends AndroidViewModel {
    private final WishlistRepository wishlistRepository;

    public interface AddToWishlistListener {
        void onSuccess();
        void onExists();
    }

    public WishlistViewModel(@NonNull Application application) {
        super(application);
        wishlistRepository = new WishlistRepository(application);
        }

    public void addToWishlist(int productId, AddToWishlistListener listener) {
        android.content.SharedPreferences prefs = getApplication().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            wishlistRepository.addToWishlist(productId, userId, new WishlistRepository.AddToWishlistCallback() {
                @Override
                public void onSuccess() {
                    if (listener != null) listener.onSuccess();
                }

                @Override
                public void onExists() {
                    if (listener != null) listener.onExists();
                }
            });
        }
    }

    public LiveData<List<Wishlist>> getWishlistByUserId(int userId) {
        return wishlistRepository.getWishlistByUserId(userId);
    }

    public void addToWishlist(Wishlist wishlist) {
        wishlistRepository.addToWishlist(wishlist);
    }

    public void removeFromWishlist(int userId, int productId) {
        wishlistRepository.removeFromWishlist(userId, productId);
    }

    public void clearWishlist(int userId) {
        wishlistRepository.clearWishlist(userId);
    }

    public LiveData<Integer> countWishlist() {
        return wishlistRepository.countWishlist();
    }

    public LiveData<List<Product>> getWishlistProducts(int userId) {
        return wishlistRepository.getWishlistProductsByUserId(userId);
    }

    public LiveData<List<Wishlist>> getAllWishlistProducts() {
        return wishlistRepository.getAllWishlist();
    }
}
