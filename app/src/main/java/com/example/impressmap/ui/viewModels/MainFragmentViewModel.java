package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.database.firebase.cases.GMarkerCommonCase;
import com.example.impressmap.database.firebase.cases.PostGMarkerCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;

import java.util.List;

public class MainFragmentViewModel extends ViewModel
{
    private GMarkerAddressCase gMarkerAddressCase;
    private GMarkerCommonCase gMarkerCommonCase;
    private PostGMarkerCase postGMarkerCase;

    public MainFragmentViewModel()
    {
        gMarkerAddressCase = new GMarkerAddressCase();
        gMarkerCommonCase = new GMarkerCommonCase();
        postGMarkerCase = new PostGMarkerCase();
    }

    public void insertAddress(Address address,
                              GMarkerMetadata gMarkerMetadata)
    {
        gMarkerAddressCase.insert(address, gMarkerMetadata);
    }

    public void insertCommonMarker(String addressId,
                                   GMarkerMetadata gMarkerMetadata,
                                   Post post)
    {
        gMarkerCommonCase.insert(addressId, gMarkerMetadata, post);
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
