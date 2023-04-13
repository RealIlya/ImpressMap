package com.example.impressmap.database.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.impressmap.model.data.GMarkerMetadata;

import java.util.List;

@Dao
public interface GMarkerMetadataDao
{
    @Query("SELECT * FROM gmarkermetadata")
    LiveData<List<GMarkerMetadata>> getAll();

    @Insert
    void insert(GMarkerMetadata gMarkerMetadata);

    @Update
    void update(GMarkerMetadata gMarkerMetadata);

    @Delete
    void delete(GMarkerMetadata gMarkerMetadata);
}
