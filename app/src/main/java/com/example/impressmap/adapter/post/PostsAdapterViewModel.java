package com.example.impressmap.adapter.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.CommentOwnerCase;
import com.example.impressmap.model.data.Owner;

import java.util.List;

public class PostsAdapterViewModel extends ViewModel
{
    private final CommentOwnerCase commentOwnerCase;

    public PostsAdapterViewModel()
    {
        commentOwnerCase = new CommentOwnerCase();
    }

    public LiveData<List<String>> getIdsByOwnerId(Owner owner)
    {
        return commentOwnerCase.getIdsByOwnerId(owner);
    }
}
