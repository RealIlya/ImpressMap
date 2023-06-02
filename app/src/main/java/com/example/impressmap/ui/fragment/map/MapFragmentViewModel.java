package com.example.impressmap.ui.fragment.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerCommonCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;

import java.util.List;

public class MapFragmentViewModel extends ViewModel
{
    private final GMarkerCommonCase gMarkerCommonCase;

    public MapFragmentViewModel()
    {
        gMarkerCommonCase = new GMarkerCommonCase();
    }

    public LiveData<List<GMarkerMetadata>> getGMarkerMetadataByAddress(Address address)
    {
        return gMarkerCommonCase.getByAddress(address);
    }
}
