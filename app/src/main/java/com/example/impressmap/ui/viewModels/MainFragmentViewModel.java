package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerCommonCase;
import com.example.impressmap.database.firebase.cases.PostGMarkerCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;

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

    public void insertCommonMarker(String addressId,
                                   GMarkerMetadata gMarkerMetadata,
                                   Post post,
                                   SuccessCallback successCallback)
    {
        gMarkerCommonCase.insert(addressId, gMarkerMetadata, post, successCallback);
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
