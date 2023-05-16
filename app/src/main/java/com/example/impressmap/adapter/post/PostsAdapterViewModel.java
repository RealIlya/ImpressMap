package com.example.impressmap.adapter.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.CommentOwnerCase;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.model.data.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapterViewModel extends ViewModel
{
    private final CommentOwnerCase commentOwnerCase;
    private final List<Post> postsCache;

    public PostsAdapterViewModel()
    {
        commentOwnerCase = new CommentOwnerCase();
        postsCache = new ArrayList<>();
    }

    public boolean addPost(Post post)
    {
        postsCache.add(post);
        return true;
    }

    public Post getPost(int index)
    {
        return postsCache.get(index);
    }

    public int getPostsCount()
    {
        return postsCache.size();
    }

    public void clearCache()
    {
        postsCache.clear();
    }

    public LiveData<List<String>> getIdsByOwnerId(Owner owner)
    {
        return commentOwnerCase.getIdsByOwner(owner);
    }
}
