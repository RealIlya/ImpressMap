package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DATE_TIME_NODE;
import static com.example.impressmap.util.Constants.Keys.GMARKER_ID;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.POSTS_NODE;
import static com.example.impressmap.util.Constants.Keys.TEXT_NODE;
import static com.example.impressmap.util.Constants.Keys.TITLE_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.PostLiveData;
import com.example.impressmap.model.data.DatabaseTransfer;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsRepo implements DatabaseRepo<Post>, DatabaseTransfer<Post>
{
    private final DatabaseReference postsRef;

    public PostsRepo()
    {
        postsRef = DATABASE_REF.child(POSTS_NODE);
    }

    @Override
    public LiveData<List<Post>> getAll()
    {
        throw new RuntimeException("Stub!");
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
        Map<String, Object> data = toMap(post);

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
    public Map<String, Object> toMap(Post post)
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, post.getId());
        data.put(OWNER_ID_NODE, post.getOwnerUser().getId());
        data.put(DATE_TIME_NODE, post.getDateTime().toEpochSecond(ZoneOffset.UTC));
        data.put(TITLE_NODE, post.getTitle());
        data.put(TEXT_NODE, post.getText());
        data.put(GMARKER_ID, post.getGMarkerId());

        return data;
    }
}
