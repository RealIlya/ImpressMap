package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.ADDRESSES_NODE;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
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
        userAddressesRef = DATABASE_REF.child(MAIN_LIST_NODE).child(UID);
    }

    @Override
    public LiveData<List<Address>> getAll()
    {
        return new AllAddressesLiveData();
    }

    @Override
    public void insert(Address address)
    {
        String addressKey = userAddressesRef.push().getKey();

        Map<String, Object> data = address.prepareToTransferToDatabase();
        data.put(CHILD_ID_NODE, addressKey);

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, addressKey);
        userAddressesRef.child(addressKey).updateChildren(sData);
        addressesRef.child(addressKey).updateChildren(data);
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
