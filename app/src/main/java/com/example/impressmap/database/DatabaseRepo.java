package com.example.impressmap.database;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface DatabaseRepo<T>
{
    LiveData<List<T>> getAll();

    void insert(T t);

    void update(T t);

    void delete(T t);
}
