package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.UserDao;
import com.example.nikeshop.data.local.entity.User;

import java.util.List;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        AppDatabase db = NikeShopApp.getDatabase();
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        Executors.newSingleThreadExecutor().execute(() -> userDao.insertUser(user));
    }

    public void delete(User user) {
        Executors.newSingleThreadExecutor().execute(() -> userDao.deleteUser(user));
    }

    public void update(User user) {
        Executors.newSingleThreadExecutor().execute(() -> userDao.updateUser(user));
    }
    public LiveData<User> getUserByIdLive(int userId) {
        return userDao.getUserByIdLive(userId);
    }

}
