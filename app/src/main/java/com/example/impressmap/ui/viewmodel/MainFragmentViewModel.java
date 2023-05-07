package com.example.impressmap.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerCommonCase;
import com.example.impressmap.database.firebase.cases.PostGMarkerCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;

import java.util.List;

public class MainFragmentViewModel extends ViewModel
{
    private final GMarkerCommonCase gMarkerCommonCase;
    private final PostGMarkerCase postGMarkerCase;

    public MainFragmentViewModel()
    {
        gMarkerCommonCase = new GMarkerCommonCase();
        postGMarkerCase = new PostGMarkerCase();
    }

    public LiveData<List<GMarkerMetadata>> getGMarkerMetadataByAddress(Address address)
    {
        return gMarkerCommonCase.getByAddress(address);
    }

    public LiveData<Post> getPostByGMarker(GMarkerMetadata gMarkerMetadata)
    {
        return postGMarkerCase.getByGMarker(gMarkerMetadata);
    }
}
