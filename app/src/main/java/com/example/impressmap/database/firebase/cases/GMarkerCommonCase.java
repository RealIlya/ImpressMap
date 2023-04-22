package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.GMarkerMetadataRepo;
import com.example.impressmap.database.firebase.repos.PostsRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class GMarkerCommonCase
{
    private final PostsRepo postsRepo;
    private GMarkerMetadataRepo gMarkerMetadataRepo;

    public GMarkerCommonCase()
    {
        this.postsRepo = new PostsRepo();
    }

    public void insert(String addressId,
                       GMarkerMetadata gMarkerMetadata,
                       Post post,
                       SuccessCallback successCallback)
    {
        gMarkerMetadataRepo = new GMarkerMetadataRepo(addressId);
        gMarkerMetadataRepo.insert(gMarkerMetadata, successCallback);
        post.setGMarkerId(gMarkerMetadata.getId());
        postsRepo.insert(post, () ->
        {
        });
    }

    public LiveData<List<GMarkerMetadata>> getByAddress(Address address)
    {
        gMarkerMetadataRepo = new GMarkerMetadataRepo(address.getId());

        return gMarkerMetadataRepo.getAll();
    }
}
