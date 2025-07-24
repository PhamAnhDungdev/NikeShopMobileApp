package com.example.nikeshop.Models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.repositories.TopSellingRepository;

import java.util.List;

public class TopSellingViewModel extends AndroidViewModel {

    private final TopSellingRepository repository;

    public TopSellingViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        repository = new TopSellingRepository(db.orderDetailDao(), db.productDao());
    }

    public LiveData<List<TopSellingProductItem>> getTopSellingProducts(int limit) {
        return repository.getTopSellingProducts(limit);
    }
}
