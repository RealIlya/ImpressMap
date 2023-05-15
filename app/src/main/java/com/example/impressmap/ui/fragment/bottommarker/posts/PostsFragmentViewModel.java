package com.example.impressmap.ui.fragment.bottommarker.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.PostGMarkerCase;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;

public class PostsFragmentViewModel extends ViewModel
{
    private final PostGMarkerCase postGMarkerCase;

    public PostsFragmentViewModel()
    {
        postGMarkerCase = new PostGMarkerCase();
    }

    public LiveData<Post> getPostByGMarker(GMarkerMetadata gMarkerMetadata)
    {
        return postGMarkerCase.getByGMarker(gMarkerMetadata);
    }
}
