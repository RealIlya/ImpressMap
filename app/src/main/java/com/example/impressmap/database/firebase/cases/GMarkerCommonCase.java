package com.example.impressmap.database.firebase.cases;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.GMarkerMetadataTable;
import com.example.impressmap.database.firebase.repos.PostsRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class GMarkerCommonCase
{
    private final PostsRepo postsRepo;
    private GMarkerMetadataTable gMarkerMetadataTable;

    public GMarkerCommonCase()
    {
        this.postsRepo = new PostsRepo();
    }

    public void insert(String addressId,
                       GMarkerMetadata gMarkerMetadata,
                       @NonNull Post post,
                       SuccessCallback successCallback)
    {
        gMarkerMetadataTable = new GMarkerMetadataTable(addressId);
        gMarkerMetadataTable.insert(gMarkerMetadata, successCallback);
        post.setGMarkerId(gMarkerMetadata.getId());
        postsRepo.insert(post, () ->
        {
        });
    }

    public LiveData<List<GMarkerMetadata>> getByAddress(@NonNull Address address)
    {
        gMarkerMetadataTable = new GMarkerMetadataTable(address.getId());

        return gMarkerMetadataTable.getAll();
    }
}
