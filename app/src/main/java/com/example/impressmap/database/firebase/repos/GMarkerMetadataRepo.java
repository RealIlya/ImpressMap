package com.example.impressmap.database.firebase.repos;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.AllGMarkerMetadataLiveData;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GMarkerMetadataRepo implements DatabaseRepo<GMarkerMetadata>
{
    private final DatabaseReference database = FirebaseDatabase.getInstance()
                                                               .getReference()
                                                               .child(Constants.AUTH.getCurrentUser()
                                                                                    .getUid());

    @Override
    public LiveData<List<GMarkerMetadata>> getAll()
    {
        return new AllGMarkerMetadataLiveData();
    }

    @Override
    public void insert(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void update(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void delete(GMarkerMetadata gMarkerMetadata)
    {

    }
}
