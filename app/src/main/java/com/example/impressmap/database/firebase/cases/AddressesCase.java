package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.AddressesTable;
import com.example.impressmap.database.firebase.repos.GMarkerMetadataTable;
import com.example.impressmap.database.firebase.repos.UserAddressesRepo;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.SuccessCallback;

import java.util.List;

public class AddressesCase
{
    private final AddressesTable addressesTable;
    private final UserAddressesRepo userAddressesRepo;
    private GMarkerMetadataTable gMarkerMetadataTable;

    public AddressesCase()
    {
        addressesTable = new AddressesTable();
        userAddressesRepo = new UserAddressesRepo();
    }

    public void join(Address address,
                     SuccessCallback successCallback)
    {
        userAddressesRepo.insert(address, successCallback);
        gMarkerMetadataTable = new GMarkerMetadataTable(address.getId());
        gMarkerMetadataTable.join(address, () ->
        {
        });
    }

    public LiveData<List<Address>> getAll()
    {
        return addressesTable.getAll();
    }
}
