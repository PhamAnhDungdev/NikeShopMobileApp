
package com.example.nikeshop.data.repositories;
import com.example.nikeshop.data.local.modelDto.CartWithProduct;
import android.content.Context;
import androidx.room.Room;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.entity.Cart;

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
    public void deleteCart(int cartId) {
        Cart cart = cartDao.getCartById(cartId);
        if (cart != null) {
            cartDao.deleteCart(cart);
        }
    }
    public void updateCartQuantity(int cartId, int newQuantity) {
        Cart cart = cartDao.getCartById(cartId);
        if (cart != null) {
            cart.setQuantity(newQuantity);
            cart.setUpdatedAt(new java.util.Date());
            cartDao.updateCart(cart);
        }
    }
    public androidx.lifecycle.LiveData<java.util.List<CartWithProduct>> getCartWithProductByUser(int userId) {
        return cartDao.getCartWithProductByUser(userId);
    }
    private final CartDao cartDao;

    public CartRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "nike_shop_db").allowMainThreadQueries().build();
        cartDao = db.cartDao();
    }

    public void addToCart(int productId, int quantity, int userId) {
        Cart existingCart = cartDao.getCartByUserAndProduct(userId, productId);
        java.util.Date now = new java.util.Date();
        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.setUpdatedAt(now);
            cartDao.updateCart(existingCart);
        } else {
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setUserId(userId);
            cart.setCreatedAt(now);
            cart.setUpdatedAt(now);
            cartDao.insertCart(cart);
        }
    }



    private final Executor executor = Executors.newSingleThreadExecutor();


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
//    public void addToCart(int userId, int productId, int quantity) {
//        executor.execute(() -> {
//            Cart existing = cartDao.getCartItem(userId, productId);
//            Date now = new Date();
//            if (existing != null) {
//                int newQty = existing.getQuantity() + quantity;
//                cartDao.updateQuantity(userId, productId, newQty, now);
//            } else {
//                Cart cart = new Cart(userId, productId, quantity, now, now, null);
//                cartDao.insert(cart);
//            }
//        });
//    }

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
