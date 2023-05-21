package com.example.impressmap.ui.fragment.addresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.AddressesCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class AddressesFragmentViewModel extends ViewModel
{
    private final AddressesCase addressesCase;

    public AddressesFragmentViewModel()
    {
        addressesCase = new AddressesCase();
    }

    public void join(Address address,
                     SuccessCallback successCallback)
    {
        addressesCase.join(address, successCallback);
    }

    public LiveData<List<Address>> getAll()
    {
        return addressesCase.getAll();
    }
}
