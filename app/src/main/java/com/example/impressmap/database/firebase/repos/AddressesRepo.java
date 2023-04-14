package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.ADDRESSES_NODE;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.AllAddressesLiveData;
import com.example.impressmap.model.data.Address;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesRepo implements DatabaseRepo<Address>
{
    private final DatabaseReference addressesRef;
    private final DatabaseReference userAddressesRef;

    public AddressesRepo()
    {
        addressesRef = DATABASE_REF.child(ADDRESSES_NODE);
        userAddressesRef = DATABASE_REF.child(MAIN_LIST_NODE).child(UID).child(ADDRESSES_NODE);
    }

    @Override
    public LiveData<List<Address>> getAll()
    {
        return new AllAddressesLiveData(userAddressesRef);
    }

    @Override
    public void insert(Address address)
    {
        String addressKey = userAddressesRef.push().getKey();

        address.setId(addressKey);
        address.setOwnerId(UID);
        Map<String, Object> data = address.prepareToTransferToDatabase();

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, addressKey);
        addressesRef.child(addressKey).updateChildren(data);
        userAddressesRef.child(addressKey).updateChildren(sData);
    }

    @Override
    public void update(Address address)
    {

    }

    @Override
    public void delete(Address address)
    {

    }
}
