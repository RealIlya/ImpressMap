package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;

public class CreatorAddressViewModel extends ViewModel
{
    private final GMarkerAddressCase gMarkerAddressCase;

    public CreatorAddressViewModel(GMarkerAddressCase gMarkerAddressCase)
    {
        this.gMarkerAddressCase = gMarkerAddressCase;
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata)
    {
        gMarkerAddressCase.insert(address, gMarkerMetadata);
    }
}
