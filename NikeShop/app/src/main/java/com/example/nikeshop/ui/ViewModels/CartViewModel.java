

package com.example.nikeshop.ui.ViewModels;
import com.example.nikeshop.data.local.modelDto.CartWithProduct;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Cart;
import com.example.nikeshop.data.repositories.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    public void deleteCart(int cartId) {
        cartRepository.deleteCart(cartId);
    }
    public void updateCartQuantity(int cartId, int newQuantity) {
        cartRepository.updateCartQuantity(cartId, newQuantity);
    }
    public androidx.lifecycle.LiveData<java.util.List<CartWithProduct>> getCartWithProductByUser(int userId) {
        return cartRepository.getCartWithProductByUser(userId);
    }
    private final CartRepository cartRepository;



    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);

    }

    // Lấy danh sách cart của user (quan sát được)
    public LiveData<List<Cart>> getCartItems(int userId) {
        return cartRepository.getCartByUser(userId);
    }

    // Thêm sản phẩm vào giỏ
    public void addToCart(int userId, int productId, int quantity) {
        cartRepository.addToCart(userId, productId, quantity);
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(int userId, int productId, int quantity) {
        cartRepository.updateQuantity(userId, productId, quantity);
    }

    public void addToCart(int productId, int quantity) {
        android.content.SharedPreferences prefs = getApplication().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            cartRepository.addToCart(productId, quantity, userId);
        }
    }

    // Xoá sản phẩm khỏi giỏ
    public void removeFromCart(int userId, int productId) {
            cartRepository.deleteItem(userId, productId);
    }

    // Xoá toàn bộ giỏ hàng
    public void clearCart(int userId) {
            cartRepository.clearCart(userId);
    }

    // Lấy tổng số sản phẩm trong giỏ
    public LiveData<Integer> getTotalItemCount(int userId) {
        return cartRepository.getTotalItemsInCart(userId);
    }
}
