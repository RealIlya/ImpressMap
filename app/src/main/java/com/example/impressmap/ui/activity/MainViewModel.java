package com.example.impressmap.ui.activity;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.FirebaseRepo;
import com.example.impressmap.database.room.AppRoomDatabase;
import com.example.impressmap.database.room.RoomBaseRepo;

public class MainViewModel extends ViewModel
{
    private DatabaseRepo databaseRepo;

    public void connectToDatabase(Context context)
    {
//        databaseRepo = new RoomBaseRepo(AppRoomDatabase.getInstance(context));
        databaseRepo = new FirebaseRepo();
        databaseRepo.connectToDatabase();
    }
}
