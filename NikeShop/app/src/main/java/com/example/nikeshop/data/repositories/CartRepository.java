
package com.example.nikeshop.data.repositories;
import com.example.nikeshop.data.local.modelDto.CartWithProduct;
import android.content.Context;
import androidx.room.Room;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.entity.Cart;

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

}
