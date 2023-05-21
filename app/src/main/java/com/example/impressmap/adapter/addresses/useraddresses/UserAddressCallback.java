package com.example.impressmap.adapter.addresses.useraddresses;

import com.example.impressmap.model.data.Address;

public interface UserAddressCallback
{
    void onAddressClick(Address address);

    void onMaxAddresses();
}
