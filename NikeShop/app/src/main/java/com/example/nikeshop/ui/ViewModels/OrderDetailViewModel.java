package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.repositories.OrderDetailRepository;

import java.util.List;

public class OrderDetailViewModel extends AndroidViewModel {

    private final OrderDetailRepository repository;

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = NikeShopApp.getDatabase();
        repository = new OrderDetailRepository(db.orderDetailDao());
    }

    public LiveData<List<OrderDetail>> getAll() {
        return repository.getAll();
    }

    public LiveData<List<OrderDetail>> getByOrderId(int orderId) {
        return repository.getByOrderId(orderId);
    }

    public void insert(OrderDetail detail) {
        repository.insert(detail);
    }

    public void insertAll(List<OrderDetail> details) {
        repository.insertAll(details);
    }

    public void delete(OrderDetail detail) {
        repository.delete(detail);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
