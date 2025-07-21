package com.example.nikeshop.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.entity.Cart;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepository {

    private final CartDao cartDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public CartRepository(Context context) {
        AppDatabase db = NikeShopApp.getDatabase();
        this.cartDao = db.cartDao();
    }

    // Lấy danh sách cart theo userId (LiveData để quan sát thay đổi)
    public LiveData<List<Cart>> getCartByUser(int userId) {
        MutableLiveData<List<Cart>> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            List<Cart> list = cartDao.getCartsByUserId(userId);
            liveData.postValue(list);
        });
        return liveData;
    }

    // Thêm 1 sản phẩm vào cart
    public void addToCart(int userId, int productId, int quantity) {
        executor.execute(() -> {
            Cart existing = cartDao.getCartItem(userId, productId);
            Date now = new Date();
            if (existing != null) {
                int newQty = existing.getQuantity() + quantity;
                cartDao.updateQuantity(userId, productId, newQty, now);
            } else {
                Cart cart = new Cart(userId, productId, quantity, now, now, null);
                cartDao.insert(cart);
            }
        });
    }

    // Cập nhật số lượng
    public void updateQuantity(int userId, int productId, int quantity) {
        executor.execute(() -> {
            Date now = new Date();
            cartDao.updateQuantity(userId, productId, quantity, now);
        });
    }

    // Xoá sản phẩm khỏi cart
    public void deleteItem(int userId, int productId) {
        executor.execute(() -> {
            cartDao.deleteCartItem(userId, productId);
        });
    }

    // Xoá toàn bộ cart của user
    public void clearCart(int userId) {
        executor.execute(() -> {
            cartDao.clearCartByUserId(userId);
        });
    }

    // Lấy tổng số sản phẩm trong giỏ hàng (dạng LiveData)
    public LiveData<Integer> getTotalItemsInCart(int userId) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            Integer total = cartDao.totalItemsInCart(userId);
            liveData.postValue(total != null ? total : 0);
        });
        return liveData;
    }
}
