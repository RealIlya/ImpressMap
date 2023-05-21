package com.example.impressmap.ui.fragment.bottommarker.showmorecomments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.CommentOwnerCase;
import com.example.impressmap.database.firebase.cases.CommentsCase;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class ShowMoreCommentsFragmentViewModel extends ViewModel
{
    private final CommentsCase commentsCase;
    private final CommentOwnerCase commentOwnerCase;

    public ShowMoreCommentsFragmentViewModel()
    {
        commentsCase = new CommentsCase();
        commentOwnerCase = new CommentOwnerCase();
    }

    public void insert(Comment comment,
                       Owner owner,
                       SuccessCallback successCallback,
                       FieldEmptyCallback fieldEmptyCallback)
    {
        if (comment.getText().isEmpty())
        {
            fieldEmptyCallback.onEmpty();
            return;
        }

        commentsCase.insert(comment, successCallback);
        commentOwnerCase.insert(comment.getId(), owner, () ->
        {
        });
    }

    public LiveData<List<String>> getIdsByOwner(Owner owner)
    {
        return commentOwnerCase.getIdsByOwner(owner);
    }

    public LiveData<Comment> getById(String commentId)
    {
        return commentsCase.getById(commentId);
    }
}
