package com.example.impressmap.adapter.addresses;

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

    public boolean setAddressList(@NonNull List<Address> addressList)
    {
        if (addressList.equals(addressesCache))
        {
            return false;
        }

        if (!addressesCache.isEmpty())
        {
            List<Address> buf = new ArrayList<>();

            for (Address address : addressList)
            {
                boolean added = false;
                for (Address addressCache : addressesCache)
                {
                    if (addressCache.equals(address))
                    {
                        buf.add(address);
                        address.setSelected(addressCache.isSelected());
                        added = true;
                        break;
                    }
                }
                if (!added)
                {
                    buf.add(address);
                }
            }

            addressesCache.clear();
            addressesCache.addAll(buf);
        }
        else
        {
            addressesCache.addAll(addressList);
        }

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
