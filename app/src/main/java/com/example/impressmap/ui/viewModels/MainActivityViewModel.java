package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;

import java.util.List;

public class MainActivityViewModel extends ViewModel
{
    private GMarkerAddressCase gMarkerAddressCase;

    public MainActivityViewModel(GMarkerAddressCase gMarkerAddressCase)
    {
        this.gMarkerAddressCase = gMarkerAddressCase;
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata)
    {
        gMarkerAddressCase.insert(address, gMarkerMetadata);
    }

    public LiveData<List<Address>> getByUser()
    {
        return gMarkerAddressCase.getByUser();
    }
}
