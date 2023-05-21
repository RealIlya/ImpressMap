package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.GMARKERS_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.AllAddressGMarkerMetadataLiveData;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMarkerMetadataRepo implements DatabaseRepo<GMarkerMetadata>
{
    private final DatabaseReference gMarkersRef;
    private final String addressId;
    private final DatabaseReference addressGMarkersRef;

    public GMarkerMetadataRepo(String addressId)
    {
        gMarkersRef = DATABASE_REF.child(GMARKERS_NODE);
        this.addressId = addressId;
        addressGMarkersRef = DATABASE_REF.child(MAIN_LIST_NODE)
                                         .child(GMARKERS_NODE)
                                         .child(this.addressId);
    }

    @Override
    public LiveData<List<GMarkerMetadata>> getAll()
    {
        return new AllAddressGMarkerMetadataLiveData(addressGMarkersRef);
    }

    @Override
    public void insert(GMarkerMetadata gMarkerMetadata,
                       SuccessCallback successCallback)
    {
        DatabaseReference push = addressGMarkersRef.push();
        String gMarkerKey = gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER ? addressId
                                                                                        : push.getKey();

        gMarkerMetadata.setId(gMarkerKey);
        Map<String, Object> data = gMarkerMetadata.prepareToTransferToDatabase();

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, gMarkerKey);

        gMarkersRef.child(gMarkerKey)
                   .updateChildren(data)
                   .addOnSuccessListener(unused -> addressGMarkersRef.child(gMarkerKey)
                                                                     .updateChildren(sData)
                                                                     .addOnSuccessListener(
                                                                             unused1 -> successCallback.onSuccess()));
    }

    public void join(Address address,
                     SuccessCallback successCallback)
    {
        String gMarkerKey = address.getId();

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, gMarkerKey);

        addressGMarkersRef.child(gMarkerKey)
                          .updateChildren(sData)
                          .addOnSuccessListener(unused1 -> successCallback.onSuccess());
    }

    @Override
    public void update(GMarkerMetadata gMarkerMetadata,
                       SuccessCallback successCallback)
    {

    }

    @Override
    public void delete(GMarkerMetadata gMarkerMetadata,
                       SuccessCallback successCallback)
    {

    }
}
