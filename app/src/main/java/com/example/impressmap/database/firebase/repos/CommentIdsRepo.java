package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.CommentIdsLiveData;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentIdsRepo implements DatabaseRepo<String>
{
    private final DatabaseReference ownerCommentsRef;

    public CommentIdsRepo(@NonNull Owner owner)
    {
        this.ownerCommentsRef = DATABASE_REF.child(MAIN_LIST_NODE)
                                            .child(OWNERS_NODE)
                                            .child(owner.getId());
    }

    @Override
    public LiveData<List<String>> getAll()
    {
        return new CommentIdsLiveData(ownerCommentsRef);
    }

    @Override
    public void insert(String id,
                       SuccessCallback successCallback)
    {
        String commentKey = ownerCommentsRef.push().getKey();

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, id);
        ownerCommentsRef.child(id)
                        .updateChildren(sData)
                        .addOnSuccessListener(unused -> successCallback.onSuccess());
    }

    @Override
    public void update(String id,
                       SuccessCallback successCallback)
    {

    }

    @Override
    public void delete(String id,
                       SuccessCallback successCallback)
    {

    }
}
