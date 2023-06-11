package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.COMMENTS_NODE;
import static com.example.impressmap.util.Constants.Keys.DATE_TIME_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.TEXT_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.CommentLiveData;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.DatabaseTransfer;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsRepo implements DatabaseRepo<Comment>, DatabaseTransfer<Comment>
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
        Map<String, Object> data = toMap(comment);

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
    public Map<String, Object> toMap(Comment comment)
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, comment.getId());
        data.put(OWNER_ID_NODE, comment.getOwnerUser().getId());
        data.put(DATE_TIME_NODE, comment.getDateTime().toEpochSecond(ZoneOffset.UTC));
        data.put(TEXT_NODE, comment.getText());

        return data;
    }
}
