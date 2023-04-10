package com.example.impressmap.database.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.impressmap.model.data.Address;

import java.util.List;

@Dao
public interface AddressDao
{
    @Query("SELECT * FROM address")
    List<Address> getAll();

    @Query("SELECT * FROM address WHERE addressid = :id")
    Address getById(String id);

    @Insert
    void insert(Address address);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);
}
