package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.COMMENTS_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.CommentLiveData;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

public class CommentsRepo implements DatabaseRepo<Comment>
{
    private final DatabaseReference commentsRef;

    public CommentsRepo()
    {
        commentsRef = DATABASE_REF.child(COMMENTS_NODE);
    }

    @Override
    public LiveData<List<Comment>> getAll()
    {
        throw new RuntimeException("Stub!");
    }

    public LiveData<Comment> getComment(String commentId)
    {
        return new CommentLiveData(commentsRef.child(commentId));
    }

    @Override
    public void insert(Comment comment,
                       SuccessCallback successCallback)
    {
        String commentKey = commentsRef.push().getKey();

        comment.setId(commentKey);
        Map<String, Object> data = comment.prepareToTransferToDatabase();

        commentsRef.child(comment.getId())
                   .updateChildren(data)
                   .addOnSuccessListener(unused -> successCallback.onSuccess());
    }

    @Override
    public void update(Comment comment,
                       SuccessCallback successCallback)
    {

    }

    @Override
    public void delete(Comment comment,
                       SuccessCallback successCallback)
    {

    }
}
