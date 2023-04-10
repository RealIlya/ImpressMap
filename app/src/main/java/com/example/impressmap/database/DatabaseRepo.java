package com.example.impressmap.database;

import androidx.lifecycle.LiveData;

import com.example.impressmap.model.Comment;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.model.data.User;

import java.util.List;

public interface DatabaseRepo
{
    LiveData<List<Address>> getAllAddresses();

    LiveData<List<Comment>> getAllComments();

    LiveData<List<Post>> getAllPosts();

    LiveData<List<User>> getAllUsers();

    LiveData<List<GMarkerMetadata>> getAllGMarkerMetadata();

    void insertAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(Address address);

    void insertComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(Comment comment);

    void insertPost(Post post);

    void updatePost(Post post);

    void deletePost(Post post);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    void insertGMarkerMetadata(GMarkerMetadata gMarkerMetadata);

    void updateGMarkerMetadata(GMarkerMetadata gMarkerMetadata);

    void deleteGMarkerMetadata(GMarkerMetadata gMarkerMetadata);
}
