package com.example.impressmap.database.firebase.cases;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.PostsRepo;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;

public class PostGMarkerCase
{
    private final PostsRepo postsRepo;

    public PostGMarkerCase()
    {
        this.postsRepo = new PostsRepo();
    }

    public LiveData<Post> getByGMarker(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        return postsRepo.getPost(gMarkerMetadata.getId());
    }
}
