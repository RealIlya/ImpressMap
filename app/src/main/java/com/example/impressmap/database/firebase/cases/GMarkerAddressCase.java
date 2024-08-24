package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.AddressesTable;
import com.example.impressmap.database.firebase.repos.GMarkerMetadataTable;
import com.example.impressmap.database.firebase.repos.UserAddressesRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class GMarkerAddressCase
{
    private final AddressesTable addressesTable;
    private final UserAddressesRepo userAddressesRepo;
    private GMarkerMetadataTable gMarkerMetadataTable;

    public GMarkerAddressCase()
    {
        addressesTable = new AddressesTable();
        userAddressesRepo = new UserAddressesRepo();
    }

    public void insert(Address address,
                       GMarkerMetadata gMarkerMetadata,
                       SuccessCallback successCallback)
    {
        addressesTable.insert(address, successCallback);
        gMarkerMetadataTable = new GMarkerMetadataTable(address.getId());
        gMarkerMetadataTable.insert(gMarkerMetadata, () ->
        {
        });
    }

    public LiveData<List<Address>> getByUser()
    {
        return userAddressesRepo.getAll();
    }
}
