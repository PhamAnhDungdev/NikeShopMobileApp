package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.nikeshop.data.local.entity.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    Long insertOrder(Order order);

    @Insert
    List<Long> insertAll(List<Order> orders);

    @Delete
    void deleteOrder(Order order);
    @Query("SELECT COUNT(*) FROM orders")
    int countOrders();

    @Query("SELECT * FROM orders WHERE deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE user_id = :userId AND deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE payment_status = :status AND deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getOrdersByStatus(String status);

    // Synchronous method for ViewModel
    @Query("SELECT * FROM orders WHERE id = :orderId AND deleted_at IS NULL")
    Order getOrderById(int orderId);

    @Query("DELETE FROM orders")
    void deleteAllOrders();
}
