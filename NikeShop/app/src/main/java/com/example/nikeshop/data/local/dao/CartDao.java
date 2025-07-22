
package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @androidx.room.Delete
    void deleteCart(Cart cart);
    @Query("SELECT * FROM carts WHERE id = :cartId LIMIT 1")
    Cart getCartById(int cartId);
    @androidx.room.Update
    void updateCart(Cart cart);
    @Query("SELECT * FROM carts WHERE user_id = :userId AND product_id = :productId AND deleted_at IS NULL LIMIT 1")
    Cart getCartByUserAndProduct(int userId, int productId);
    @Insert
    long insertCart(Cart cart);
    @Insert
    List<Long> insertAll(List<Cart> carts);
    @Query("SELECT COUNT(*) FROM carts")
    int countCarts();

    @Query("SELECT carts.id as cartId, carts.user_id as userId, carts.product_id as productId, carts.quantity as quantity, products.name as productName, products.image_url as productImageUrl, products.price as productPrice, (products.price * carts.quantity) as totalPrice FROM carts INNER JOIN products ON carts.product_id = products.id WHERE carts.user_id = :userId AND carts.deleted_at IS NULL")
    androidx.lifecycle.LiveData<java.util.List<com.example.nikeshop.data.local.modelDto.CartWithProduct>> getCartWithProductByUser(int userId);

        
}
