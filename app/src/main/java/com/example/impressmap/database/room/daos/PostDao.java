package com.example.impressmap.database.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.impressmap.model.data.Post;

import java.util.List;

@Dao
public interface PostDao
{
    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * FROM post WHERE postid = :id")
    Post getById(String id);

    void insert(Post post);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);
}
