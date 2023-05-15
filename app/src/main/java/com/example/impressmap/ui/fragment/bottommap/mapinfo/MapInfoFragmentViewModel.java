package com.example.impressmap.ui.fragment.bottommap.mapinfo;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerCommonCase;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapInfoFragmentViewModel extends ViewModel
{
    private final GMarkerCommonCase gMarkerCommonCase;

    private LatLng latLng;

    public MapInfoFragmentViewModel()
    {
        gMarkerCommonCase = new GMarkerCommonCase();
    }

    public void insertCommonMarker(String addressId,
                                   GMarkerMetadata gMarkerMetadata,
                                   Post post,
                                   SuccessCallback successCallback)
    {
        gMarkerCommonCase.insert(addressId, gMarkerMetadata, post, successCallback);
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }
}
