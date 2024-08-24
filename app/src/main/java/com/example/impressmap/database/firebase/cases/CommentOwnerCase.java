package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.CommentIdsTable;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class CommentOwnerCase
{
    private CommentIdsTable commentIdsTable;

    public void insert(String id,
                       Owner owner,
                       SuccessCallback successCallback)
    {
        commentIdsTable = new CommentIdsTable(owner);
        commentIdsTable.insert(id, successCallback);
    }

    public LiveData<List<String>> getIdsByOwner(Owner owner)
    {
        commentIdsTable = new CommentIdsTable(owner);
        return commentIdsTable.getAll();
    }
}
