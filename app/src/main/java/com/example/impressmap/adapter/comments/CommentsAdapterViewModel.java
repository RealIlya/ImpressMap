package com.example.impressmap.adapter.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.CommentOwnerCase;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.Owner;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapterViewModel extends ViewModel
{
    private final CommentOwnerCase commentOwnerCase;
    private final List<Comment> commentsCache;

    public CommentsAdapterViewModel()
    {
        commentOwnerCase = new CommentOwnerCase();
        commentsCache = new ArrayList<>();
    }

    public boolean addComment(Comment comment)
    {
        commentsCache.add(comment);
        return true;
    }

    public Comment getComment(int index)
    {
        return commentsCache.get(index);
    }

    public int getCommentsCount()
    {
        return commentsCache.size();
    }

    public void clearCache()
    {
        commentsCache.clear();
    }

    public LiveData<List<String>> getIdsByOwnerId(Owner owner)
    {
        return commentOwnerCase.getIdsByOwner(owner);
    }
}
