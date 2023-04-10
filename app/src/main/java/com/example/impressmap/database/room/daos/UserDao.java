package com.example.impressmap.database.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.impressmap.model.data.User;

import java.util.List;

@Dao
public interface UserDao
{
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE userid = :id")
    User getById(String id);

    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
