package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.POSTS_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.PostLiveData;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

public class PostsRepo implements DatabaseRepo<Post>
{
    private final DatabaseReference postsRef;

    public PostsRepo()
    {
        postsRef = DATABASE_REF.child(POSTS_NODE);
    }

    @Override
    public LiveData<List<Post>> getAll()
    {
        return null;
    }

    public LiveData<Post> getPost(String postId)
    {
        return new PostLiveData(postsRef.child(postId));
    }

    @Override
    public void insert(Post post,
                       SuccessCallback successCallback)
    {
        post.setId(post.getGMarkerId());
        Map<String, Object> data = post.prepareToTransferToDatabase();

        postsRef.child(post.getGMarkerId())
                .updateChildren(data)
                .addOnSuccessListener(unused -> successCallback.onSuccess());
    }

    @Override
    public void update(Post post,
                       SuccessCallback successCallback)
    {

    }

    @Override
    public void delete(Post post,
                       SuccessCallback successCallback)
    {

    }
}
