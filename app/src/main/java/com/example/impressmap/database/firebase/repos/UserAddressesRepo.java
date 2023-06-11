package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.ADDRESSES_NODE;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.AllUserAddressesLiveData;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAddressesRepo implements DatabaseRepo<Address>
{
    private final DatabaseReference userAddressesRef;

    public UserAddressesRepo()
    {
        userAddressesRef = DATABASE_REF.child(MAIN_LIST_NODE)
                                       .child(USERS_NODE)
                                       .child(UID)
                                       .child(ADDRESSES_NODE);
    }

    @Override
    public LiveData<List<Address>> getAll()
    {
        return new AllUserAddressesLiveData(userAddressesRef);
    }

    @Override
    public void insert(Address address,
                       SuccessCallback successCallback)
    {
        String addressKey = address.getId();

        address.setOwnerId(UID);

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, addressKey);
        userAddressesRef.child(addressKey)
                        .updateChildren(sData)
                        .addOnSuccessListener(unused1 -> successCallback.onSuccess());
    }

    @Override
    public void update(Address address,
                       SuccessCallback successCallback)
    {

    }

}
