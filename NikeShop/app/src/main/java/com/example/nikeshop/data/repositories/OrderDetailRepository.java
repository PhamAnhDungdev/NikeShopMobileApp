package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.entity.OrderDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDetailRepository {

    private final OrderDetailDao orderDetailDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public OrderDetailRepository(OrderDetailDao dao) {
        this.orderDetailDao = dao;
    }

    public LiveData<List<OrderDetail>> getAll() {
        return orderDetailDao.getAll();
    }

    public LiveData<List<OrderDetail>> getByOrderId(int orderId) {
        return orderDetailDao.getByOrderId(orderId);
    }

    public void insert(OrderDetail detail) {
        executor.execute(() -> orderDetailDao.insert(detail));
    }

    public void insertAll(List<OrderDetail> details) {
        executor.execute(() -> orderDetailDao.insertAll(details));
    }

    public void delete(OrderDetail detail) {
        executor.execute(() -> orderDetailDao.delete(detail));
    }

    public void deleteAll() {
        executor.execute(orderDetailDao::deleteAll);
    }
}
