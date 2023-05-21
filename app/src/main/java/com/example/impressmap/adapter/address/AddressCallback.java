package com.example.impressmap.adapter.address;

import com.example.impressmap.model.data.Address;

public interface AddressCallback
{
    void onAddressClick(Address address);

    void onMaxAddresses();
}
