package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.model.data.Address;

import java.util.List;

public class AddressesFragmentViewModel extends ViewModel
{
    private final GMarkerAddressCase gMarkerAddressCase;

    public AddressesFragmentViewModel()
    {
        gMarkerAddressCase = new GMarkerAddressCase();
    }

    public LiveData<List<Address>> getByUser()
    {
        return gMarkerAddressCase.getByUser();
    }
}
