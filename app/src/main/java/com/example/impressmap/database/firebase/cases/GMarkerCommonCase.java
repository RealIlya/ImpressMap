package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.GMarkerMetadataRepo;
import com.example.impressmap.database.firebase.repos.PostsRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;

import java.util.List;

public class GMarkerCommonCase
{
    private GMarkerMetadataRepo gMarkerMetadataRepo;
    private final PostsRepo postsRepo;

    public GMarkerCommonCase()
    {
        this.postsRepo = new PostsRepo();
    }

    public void insert(String addressId,
                       GMarkerMetadata gMarkerMetadata,
                       Post post)
    {
        gMarkerMetadataRepo = new GMarkerMetadataRepo(addressId);
        gMarkerMetadataRepo.insert(gMarkerMetadata);
        post.setGMarkerId(gMarkerMetadata.getId());
        postsRepo.insert(post);
    }

    public LiveData<List<GMarkerMetadata>> getByAddress(Address address)
    {
        gMarkerMetadataRepo = new GMarkerMetadataRepo(address.getId());

        return gMarkerMetadataRepo.getAll();
    }
}
