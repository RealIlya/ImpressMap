package com.example.impressmap.database.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.impressmap.model.data.Comment;

import java.util.List;

@Dao
public interface CommentDao
{
    @Query("SELECT * FROM comment")
    LiveData<List<Comment>> getAll();

    @Query("SELECT * FROM comment WHERE commentid = :id")
    Comment getById(long id);

    @Insert
    void insert(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);
}
