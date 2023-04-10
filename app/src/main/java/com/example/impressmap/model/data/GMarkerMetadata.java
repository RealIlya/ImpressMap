package com.example.impressmap.model.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

// for database store
@Entity
public class GMarkerMetadata
{
    public static final int ADDRESS_MARKER = 0, COMMON_MARKER = 1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mmid")
    private long id;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private LatLng latLng;
    @ColumnInfo
    private int type;

    private String firebaseId;

    public GMarkerMetadata(long id,
                           String title,
                           LatLng latLng,
                           int type)
    {
        this.id = id;
        this.title = title;
        this.latLng = latLng;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public int getType()
    {
        return type;
    }
}
