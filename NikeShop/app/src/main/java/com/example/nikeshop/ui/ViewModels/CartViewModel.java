package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Cart;
import com.example.nikeshop.data.repositories.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository repository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new CartRepository(application);
    }

    // Lấy danh sách cart của user (quan sát được)
    public LiveData<List<Cart>> getCartItems(int userId) {
        return repository.getCartByUser(userId);
    }

    // Thêm sản phẩm vào giỏ
    public void addToCart(int userId, int productId, int quantity) {
        repository.addToCart(userId, productId, quantity);
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(int userId, int productId, int quantity) {
        repository.updateQuantity(userId, productId, quantity);
    }

    // Xoá sản phẩm khỏi giỏ
    public void removeFromCart(int userId, int productId) {
        repository.deleteItem(userId, productId);
    }

    // Xoá toàn bộ giỏ hàng
    public void clearCart(int userId) {
        repository.clearCart(userId);
    }

    // Lấy tổng số sản phẩm trong giỏ
    public LiveData<Integer> getTotalItemCount(int userId) {
        return repository.getTotalItemsInCart(userId);
    }
}
