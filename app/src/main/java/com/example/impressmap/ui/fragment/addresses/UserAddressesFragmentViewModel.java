package com.example.impressmap.ui.fragment.addresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.GMarkerAddressCase;
import com.example.impressmap.model.data.Address;

import java.util.List;

public class UserAddressesFragmentViewModel extends ViewModel
{
    private final GMarkerAddressCase gMarkerAddressCase;

    private final MutableLiveData<Integer> addressesCount;
    private final MutableLiveData<Integer> selectedAddressesCount;

    public UserAddressesFragmentViewModel()
    {
        gMarkerAddressCase = new GMarkerAddressCase();

        addressesCount = new MutableLiveData<>(0);
        selectedAddressesCount = new MutableLiveData<>(0);
    }

    public LiveData<List<Address>> getByUser()
    {
        return gMarkerAddressCase.getByUser();
    }

    public LiveData<Integer> getAddressesCount()
    {
        return addressesCount;
    }

    public void setAddressesCount(int addressesCount)
    {
        this.addressesCount.setValue(addressesCount);
    }

    public LiveData<Integer> getSelectedAddressesCount()
    {
        return selectedAddressesCount;
    }

    public void setSelectedAddressesCount(int selectedAddressesCount)
    {
        this.selectedAddressesCount.setValue(selectedAddressesCount);
    }
}
