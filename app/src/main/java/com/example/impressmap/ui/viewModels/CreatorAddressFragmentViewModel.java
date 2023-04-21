package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;

public class CreatorAddressFragmentViewModel extends ViewModel
{
    private final GMarkerAddressCase gMarkerAddressCase;

    public CreatorAddressFragmentViewModel()
    {
        gMarkerAddressCase = new GMarkerAddressCase();
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata)
    {
        gMarkerAddressCase.insert(address, gMarkerMetadata);
    }
}
