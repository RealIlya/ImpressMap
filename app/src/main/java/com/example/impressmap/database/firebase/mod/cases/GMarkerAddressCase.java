package com.example.impressmap.database.firebase.mod.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.mod.repos.AddressesRepo;
import com.example.impressmap.database.firebase.mod.repos.UserAddressesRepo;
import com.example.impressmap.database.firebase.mod.repos.GMarkerMetadataRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class GMarkerAddressCase
{
    private final AddressesRepo addressesRepo;
    private final UserAddressesRepo userAddressesRepo;
    private GMarkerMetadataRepo gMarkerMetadataRepo;

    public GMarkerAddressCase()
    {
        addressesRepo = new AddressesRepo();
        userAddressesRepo = new UserAddressesRepo();
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata,
                       SuccessCallback successCallback)
    {
        addressesRepo.insert(address, successCallback);
        gMarkerMetadataRepo = new GMarkerMetadataRepo(address.getId());
        gMarkerMetadataRepo.insert(gMarkerMetadata, () ->
        {
        });
    }

    public LiveData<List<Address>> getByUser()
    {
        return userAddressesRepo.getAll();
    }
}
