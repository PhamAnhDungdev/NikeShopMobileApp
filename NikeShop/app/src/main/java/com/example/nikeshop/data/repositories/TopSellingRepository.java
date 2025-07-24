package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.modelDto.TopSellingProductDto;
import com.example.nikeshop.Models.TopSellingProductItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TopSellingRepository {
    private final OrderDetailDao orderDetailDao;
    private final ProductDao productDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TopSellingRepository(OrderDetailDao orderDetailDao, ProductDao productDao) {
        this.orderDetailDao = orderDetailDao;
        this.productDao = productDao;
    }

    public LiveData<List<TopSellingProductItem>> getTopSellingProducts(int limit) {
        MutableLiveData<List<TopSellingProductItem>> result = new MutableLiveData<>();
        executor.execute(() -> {
            List<TopSellingProductDto> topList = orderDetailDao.getTopSellingProducts(limit);
            List<TopSellingProductItem> displayItems = new ArrayList<>();

            for (TopSellingProductDto dto : topList) {
                Product product = productDao.getProductById(dto.productId);
                if (product != null) {
                    displayItems.add(new TopSellingProductItem(
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice(),
                            dto.totalSold
                    ));
                }
            }

            result.postValue(displayItems);
        });
        return result;
    }
}
