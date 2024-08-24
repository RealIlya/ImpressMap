package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.CommentsTable;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.util.SuccessCallback;

public class CommentsCase
{
    private final CommentsTable commentsTable;

    public CommentsCase()
    {
        commentsTable = new CommentsTable();
    }

    public void insert(Comment comment,
                       SuccessCallback successCallback)
    {
        commentsTable.insert(comment, successCallback);
    }

    public LiveData<Comment> getById(String commentId)
    {
        return commentsTable.getComment(commentId);
    }
}
