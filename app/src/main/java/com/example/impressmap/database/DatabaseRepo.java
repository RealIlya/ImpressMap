package com.example.impressmap.database;

import androidx.lifecycle.LiveData;

import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public interface DatabaseRepo<T>
{
    void insert(T t,
                SuccessCallback successCallback);

    void update(T t,
                SuccessCallback successCallback);

}
