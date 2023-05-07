package com.example.impressmap.adapter.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.model.data.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressesAdapterViewModel extends ViewModel
{
    private final List<Address> addressesCache;

    public AddressesAdapterViewModel()
    {
        this.addressesCache = new ArrayList<>();
    }

    public boolean setAddresses(@NonNull List<Address> addresses)
    {
        if (addresses.equals(addressesCache))
        {
            return false;
        }

        addressesCache.addAll(addresses);
        return true;
    }

    public Address getAddress(int index)
    {
        return addressesCache.get(index);
    }

    public int getAddressesCount()
    {
        return addressesCache.size();
    }

    public List<Address> getSelectedAddresses()
    {
        List<Address> selectedAddresses = new ArrayList<>();

        for (Address address : addressesCache)
        {
            if (address.isSelected())
            {
                selectedAddresses.add(address);
            }
        }

        return selectedAddresses;
    }
}
