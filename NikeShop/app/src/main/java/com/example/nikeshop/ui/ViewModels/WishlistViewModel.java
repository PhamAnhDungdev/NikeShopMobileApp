package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.Wishlist;
import com.example.nikeshop.data.repositories.WishlistRepository;

import java.util.List;

public class WishlistViewModel extends AndroidViewModel {

    private final WishlistRepository repository;

    public WishlistViewModel(@NonNull Application application) {
        super(application);
        repository = new WishlistRepository(application);
    }

    public LiveData<List<Wishlist>> getWishlistByUserId(int userId) {
        return repository.getWishlistByUserId(userId);
    }

    public void addToWishlist(Wishlist wishlist) {
        repository.addToWishlist(wishlist);
    }

    public void removeFromWishlist(int userId, int productId) {
        repository.removeFromWishlist(userId, productId);
    }

    public void clearWishlist(int userId) {
        repository.clearWishlist(userId);
    }

    public LiveData<Integer> countWishlist() {
        return repository.countWishlist();
    }

    public LiveData<List<Product>> getWishlistProducts(int userId) {
        return repository.getWishlistProductsByUserId(userId);
    }

    public LiveData<List<Wishlist>> getAllWishlistProducts() {
        return repository.getAllWishlist();
    }
}
