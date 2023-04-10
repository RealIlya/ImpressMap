package com.example.impressmap.model.data;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity
public class Address
{
    @PrimaryKey
    @ColumnInfo(name = "addressid")
    private String id;

    @Embedded
    private User owner;
//    @TypeConverters(StringListConverter.class)
    private List<String> userIds;
    private String country;
    private String city;
    private String street;
    private String house;
}
