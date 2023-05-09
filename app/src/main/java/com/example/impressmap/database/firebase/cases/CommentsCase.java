package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.CommentIdsRepo;
import com.example.impressmap.database.firebase.repos.CommentsRepo;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.util.SuccessCallback;

public class CommentsCase
{
    private final CommentsRepo commentsRepo;

    public CommentsCase()
    {
        commentsRepo = new CommentsRepo();
    }

    public void insert(Comment comment,
                       SuccessCallback successCallback)
    {
        commentsRepo.insert(comment, successCallback);
    }

    public LiveData<Comment> getById(String commentId)
    {
        return commentsRepo.getComment(commentId);
    }
}
