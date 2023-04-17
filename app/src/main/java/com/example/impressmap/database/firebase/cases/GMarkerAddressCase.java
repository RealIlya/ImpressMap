package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.AddressesRepo;
import com.example.impressmap.database.firebase.repos.GMarkerMetadataRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.User;

import java.util.List;

public class GMarkerAddressCase
{
    private final AddressesRepo addressesRepo;
    private GMarkerMetadataRepo gMarkerMetadataRepo;

    public GMarkerAddressCase()
    {
        this.addressesRepo = new AddressesRepo();
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata)
    {
        addressesRepo.insert(address);
        gMarkerMetadataRepo = new GMarkerMetadataRepo(address.getId());
        gMarkerMetadataRepo.insert(gMarkerMetadata);
    }

    public LiveData<List<Address>> getByUser()
    {
        return addressesRepo.getAll();
    }
}
