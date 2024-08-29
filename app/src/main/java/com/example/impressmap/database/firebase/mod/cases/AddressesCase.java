package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.AddressesRepo;
import com.example.impressmap.database.firebase.repos.GMarkerMetadataRepo;
import com.example.impressmap.database.firebase.repos.UserAddressesRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class AddressesCase
{
    private final AddressesRepo addressesRepo;
    private final UserAddressesRepo userAddressesRepo;
    private GMarkerMetadataRepo gMarkerMetadataRepo;

    public AddressesCase()
    {
        addressesRepo = new AddressesRepo();
        userAddressesRepo = new UserAddressesRepo();
    }

    public void join(Address address,
                     SuccessCallback successCallback)
    {
        userAddressesRepo.insert(address, successCallback);
        gMarkerMetadataRepo = new GMarkerMetadataRepo(address.getId());
        gMarkerMetadataRepo.join(address, () ->
        {
        });
    }

    public LiveData<List<Address>> getAll()
    {
        return addressesRepo.getAll();
    }
}
