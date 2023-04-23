package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.model.data.Address;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel
{
    public static final int MAX_ADDRESSES_COUNT = 8;

    private final MutableLiveData<Integer> mode = new MutableLiveData<>();
    private final MutableLiveData<List<Address>> selectedAddresses = new MutableLiveData<>();
    private final MutableLiveData<String> selectedAddressId = new MutableLiveData<>("");

    public MainViewModel()
    {
        selectedAddresses.setValue(new ArrayList<>());
    }

    public LiveData<Integer> getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode.setValue(mode);
    }

    public LiveData<List<Address>> getSelectedAddresses()
    {
        return selectedAddresses;
    }

    public LiveData<String> getSelectedAddressId()
    {
        return selectedAddressId;
    }

    public void setSelectedAddressId(String addressId)
    {
        selectedAddressId.setValue(addressId);
    }

    public void switchSelectionAddress(Address address)
    {
        List<Address> addresses = selectedAddresses.getValue();

        if (addresses == null || (addresses.size() > MAX_ADDRESSES_COUNT && !address.isSelected()))
        {
            return;
        }

        if (address.isSelected())
        {
            addresses.remove(address);
            address.setSelected(false);
        }
        else
        {
            addresses.add(address);
            address.setSelected(true);
        }

        selectedAddresses.setValue(addresses);
    }
}
