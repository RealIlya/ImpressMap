package com.example.impressmap.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.impressmap.database.room.daos.AddressDao;
import com.example.impressmap.database.room.daos.CommentDao;
import com.example.impressmap.database.room.daos.GMarkerMetadataDao;
import com.example.impressmap.database.room.daos.PostDao;
import com.example.impressmap.database.room.daos.UserDao;
import com.example.impressmap.model.Comment;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.util.Constants;

@Database(entities = {Address.class, User.class, Post.class, Comment.class,
        GMarkerMetadata.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase
{
    public abstract AddressDao addressDao();

    public abstract UserDao userDao();

    public abstract PostDao postDao();

    public abstract CommentDao commentDao();

    public abstract GMarkerMetadataDao gMarkerMetadataDao();

    private static AppRoomDatabase instance = null;

    public static AppRoomDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = (AppRoomDatabase) Room.databaseBuilder(context, RoomDatabase.class,
                    Constants.DATABASE_NAME).build();
        }

        return instance;
    }
}
