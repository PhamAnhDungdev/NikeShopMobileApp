
package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Cart;
import com.example.nikeshop.data.local.entity.Product;

import java.util.Date;
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

    // Thêm nhiều sản phẩm vào giỏ hàng
    @Insert
    List<Long> insertAll(List<Cart> carts);

    // Thêm một sản phẩm vào giỏ
    @Insert
    long insert(Cart cart);

    // Lấy toàn bộ sản phẩm trong giỏ của 1 user
    @Query("SELECT * FROM carts WHERE user_id = :userId AND deleted_at IS NULL")
    List<Cart> getCartsByUserId(int userId);

    // Lấy thông tin giỏ hàng theo userId + productId (để kiểm tra có tồn tại không)
    @Query("SELECT * FROM carts WHERE user_id = :userId AND product_id = :productId AND deleted_at IS NULL LIMIT 1")
    Cart getCartItem(int userId, int productId);

    // Cập nhật số lượng sản phẩm trong giỏ
    @Query("UPDATE carts SET quantity = :quantity, updated_at = :updatedAt WHERE user_id = :userId AND product_id = :productId")
    void updateQuantity(int userId, int productId, int quantity, Date updatedAt);

    // Cập nhật toàn bộ cart (cả object)
    @Query("UPDATE carts SET quantity = :quantity, updated_at = :updatedAt WHERE id = :cartId")
    void updateCart(int cartId, int quantity, Date updatedAt);

    // Xoá 1 sản phẩm khỏi giỏ
    @Query("DELETE FROM carts WHERE user_id = :userId AND product_id = :productId")
    void deleteCartItem(int userId, int productId);

    // Xoá tất cả giỏ hàng của 1 user
    @Query("DELETE FROM carts WHERE user_id = :userId")
    void clearCartByUserId(int userId);

    // Xoá theo ID
    @Query("DELETE FROM carts WHERE id = :id")
    void deleteById(int id);

    // Đếm tổng số dòng trong giỏ (toàn bộ hệ thống)
    @Query("SELECT COUNT(*) FROM carts")
    int countCarts();

    @Query("SELECT carts.id as cartId, carts.user_id as userId, carts.product_id as productId, carts.quantity as quantity, products.name as productName, products.image_url as productImageUrl, products.price as productPrice, (products.price * carts.quantity) as totalPrice FROM carts INNER JOIN products ON carts.product_id = products.id WHERE carts.user_id = :userId AND carts.deleted_at IS NULL")
    androidx.lifecycle.LiveData<java.util.List<com.example.nikeshop.data.local.modelDto.CartWithProduct>> getCartWithProductByUser(int userId);



    // Tổng số sản phẩm trong giỏ của user (theo quantity)
    @Query("SELECT SUM(quantity) FROM carts WHERE user_id = :userId")
    Integer totalItemsInCart(int userId);

//    @Query("SELECT p.*, c.quantity FROM carts c INNER JOIN products p ON c.product_id = p.id WHERE c.user_id = :userId AND c.deleted_at IS NULL")
//    List<Product> getCartWithProducts(int userId);

}

