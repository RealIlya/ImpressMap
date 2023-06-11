package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.ADDRESSES_NODE;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DESC_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.POSITION_NODE;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.AllAddressesLiveData;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.DatabaseTransfer;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesRepo implements DatabaseRepo<Address>, DatabaseTransfer<Address>
{
    private final DatabaseReference addressesRef;
    private final DatabaseReference userAddressesRef;

    public AddressesRepo()
    {
        addressesRef = DATABASE_REF.child(ADDRESSES_NODE);
        userAddressesRef = DATABASE_REF.child(MAIN_LIST_NODE)
                                       .child(USERS_NODE)
                                       .child(UID)
                                       .child(ADDRESSES_NODE);
    }

    @Override
    public LiveData<List<Address>> getAll()
    {
        return new AllAddressesLiveData(addressesRef);
    }

    @Override
    public void insert(Address address,
                       SuccessCallback successCallback)
    {
        String addressKey = userAddressesRef.push().getKey();

        address.setId(addressKey);
        address.setOwnerId(UID);
        Map<String, Object> data = toMap(address);

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, addressKey);
        addressesRef.child(addressKey)
                    .updateChildren(data)
                    .addOnSuccessListener(unused -> userAddressesRef.child(addressKey)
                                                                    .updateChildren(sData)
                                                                    .addOnSuccessListener(
                                                                            unused1 -> successCallback.onSuccess()));
    }

    @Override
    public void update(Address address,
                       SuccessCallback successCallback)
    {

    }

    @Override
    public Map<String, Object> toMap(Address address)
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, address.getId());
        data.put(DESC_NODE, address.getDesc());
        data.put(OWNER_ID_NODE, address.getOwnerId());
        data.put(POSITION_NODE, address.getPosition());

        return data;
    }
}
