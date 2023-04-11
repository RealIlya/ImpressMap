package com.example.impressmap.database.room;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
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

import java.util.List;

public class RoomBaseRepo implements DatabaseRepo
{
    private final AppRoomDatabase database;

    private AddressDao addressDao;
    private UserDao userDao;
    private PostDao postDao;
    private CommentDao commentDao;
    private GMarkerMetadataDao gMarkerMetadataDao;


    public RoomBaseRepo(AppRoomDatabase database)
    {
        this.database = database;

        addressDao = database.addressDao();
        userDao = database.userDao();
        postDao = database.postDao();
        commentDao = database.commentDao();
        gMarkerMetadataDao = database.gMarkerMetadataDao();
    }

    @Override
    public LiveData<List<Address>> getAllAddresses()
    {
        return null;
    }

    @Override
    public LiveData<List<Comment>> getAllComments()
    {
        return null;
    }

    @Override
    public LiveData<List<Post>> getAllPosts()
    {
        return null;
    }

    @Override
    public LiveData<List<User>> getAllUsers()
    {
        return null;
    }

    @Override
    public LiveData<List<GMarkerMetadata>> getAllGMarkerMetadata()
    {
        return gMarkerMetadataDao.getAll();
    }

    @Override
    public void insertAddress(Address address)
    {
        addressDao.insert(address);
    }

    @Override
    public void updateAddress(Address address)
    {
        addressDao.update(address);
    }

    @Override
    public void deleteAddress(Address address)
    {
        addressDao.delete(address);
    }

    @Override
    public void insertComment(Comment comment)
    {
        commentDao.insert(comment);
    }

    @Override
    public void updateComment(Comment comment)
    {
        commentDao.update(comment);
    }

    @Override
    public void deleteComment(Comment comment)
    {
        commentDao.delete(comment);
    }

    @Override
    public void insertPost(Post post)
    {
        postDao.insert(post);
    }

    @Override
    public void updatePost(Post post)
    {
        postDao.update(post);
    }

    @Override
    public void deletePost(Post post)
    {
        postDao.delete(post);
    }

    @Override
    public void insertUser(User user)
    {
        userDao.insert(user);
    }

    @Override
    public void updateUser(User user)
    {
        userDao.update(user);
    }

    @Override
    public void deleteUser(User user)
    {
        userDao.delete(user);
    }

    @Override
    public void insertGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {
        gMarkerMetadataDao.insert(gMarkerMetadata);
    }

    @Override
    public void updateGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {
        gMarkerMetadataDao.update(gMarkerMetadata);
    }

    @Override
    public void deleteGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {
        gMarkerMetadataDao.delete(gMarkerMetadata);
    }
}
