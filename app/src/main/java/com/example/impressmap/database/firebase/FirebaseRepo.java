package com.example.impressmap.database.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.model.Comment;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseRepo implements DatabaseRepo
{
    private final DatabaseReference database = FirebaseDatabase.getInstance()
                                                               .getReference()
                                                               .child(Constants.AUTH.getCurrentUser()
                                                                                    .getUid());

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
        return new AllGMarkerMetadataLiveData();
    }

    @Override
    public void insertAddress(Address address)
    {

    }

    @Override
    public void updateAddress(Address address)
    {

    }

    @Override
    public void deleteAddress(Address address)
    {

    }

    @Override
    public void insertComment(Comment comment)
    {

    }

    @Override
    public void updateComment(Comment comment)
    {

    }

    @Override
    public void deleteComment(Comment comment)
    {

    }

    @Override
    public void insertPost(Post post)
    {

    }

    @Override
    public void updatePost(Post post)
    {

    }

    @Override
    public void deletePost(Post post)
    {

    }

    @Override
    public void insertUser(User user)
    {

    }

    @Override
    public void updateUser(User user)
    {

    }

    @Override
    public void deleteUser(User user)
    {

    }

    @Override
    public void insertGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void updateGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void deleteGMarkerMetadata(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void signOut()
    {
        Constants.AUTH.signOut();
    }

    @Override
    public void connectToDatabase()
    {
        String email = Constants.EMAIL;
        String password = Constants.PASSWORD;
        Constants.AUTH.signInWithEmailAndPassword(email, password)
                      .addOnFailureListener(new OnFailureListener()
                      {
                          @Override
                          public void onFailure(@NonNull Exception e)
                          {
                              Constants.AUTH.createUserWithEmailAndPassword(email, password);
                          }
                      });
    }
}
