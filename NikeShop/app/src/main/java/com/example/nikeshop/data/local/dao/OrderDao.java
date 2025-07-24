package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nikeshop.data.local.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    Long insertOrder(Order order);

    @Insert
    List<Long> insertAll(List<Order> orders);

    @Update
    void updateOrder(Order order);

    @Delete
    void deleteOrder(Order order);

    @Query("DELETE FROM orders")
    void deleteAllOrders();

    @Query("SELECT COUNT(*) FROM orders")
    int countOrders();

    @Query("SELECT * FROM orders WHERE deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE user_id = :userId AND deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE payment_status = :status AND deleted_at IS NULL ORDER BY created_at DESC")
    LiveData<List<Order>> getOrdersByStatus(String status);

    @Query("SELECT * FROM orders WHERE id = :orderId AND deleted_at IS NULL")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE id = :orderId AND deleted_at IS NULL")
    LiveData<Order> getOrderByIdLive(int orderId);

    @Query("SELECT * FROM orders WHERE payment_status LIKE '%' || :keyword || '%' OR payment_method LIKE '%' || :keyword || '%' ORDER BY created_at DESC")
    LiveData<List<Order>> searchOrders(String keyword);


    @Query("SELECT * FROM orders WHERE payment_status = :status AND deleted_at IS NULL ORDER BY created_at DESC")
    List<Order> getOrdersByStatusSync(String status); // <-- thêm dòng này

    @Query("SELECT * FROM orders WHERE payment_status LIKE '%' || :keyword || '%' OR payment_method LIKE '%' || :keyword || '%' ORDER BY created_at DESC")
    List<Order> searchOrdersSync(String keyword); // <-- thêm dòng này

    @Query("SELECT * FROM orders WHERE deleted_at IS NULL ORDER BY created_at DESC")
    List<Order> getAllOrdersSync();  // <-- bổ sung dòng này

}

