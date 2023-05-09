package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.CommentIdsRepo;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class CommentOwnerCase
{
    private CommentIdsRepo commentIdsRepo;

    public void insert(String id,
                       Owner owner,
                       SuccessCallback successCallback)
    {
        commentIdsRepo = new CommentIdsRepo(owner);
        commentIdsRepo.insert(id, successCallback);
    }

    public LiveData<List<String>> getIdsByOwnerId(Owner owner)
    {
        commentIdsRepo = new CommentIdsRepo(owner);
        return commentIdsRepo.getAll();
    }
}
