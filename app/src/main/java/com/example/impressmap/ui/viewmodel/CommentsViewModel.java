package com.example.impressmap.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.CommentCase;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.util.SuccessCallback;

public class CommentsViewModel extends ViewModel
{
    private final CommentCase commentCase;

    public CommentsViewModel()
    {
        commentCase = new CommentCase();
    }

    public void insert(Comment comment,
                       SuccessCallback successCallback)
    {
        commentCase.insert(comment, successCallback);
    }

    public LiveData<Comment> getById(String commentId)
    {
        return commentCase.getById(commentId);
    }
}
