package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.GMARKERS_NODE;
import static com.example.impressmap.util.Constants.Keys.MAIN_LIST_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.AllGMarkerMetadataLiveData;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMarkerMetadataRepo implements DatabaseRepo<GMarkerMetadata>
{
    private final DatabaseReference gMarkersRef;
    private DatabaseReference userGMarkersRef;

    public GMarkerMetadataRepo(String addressNode)
    {
        gMarkersRef = DATABASE_REF.child(GMARKERS_NODE);
        userGMarkersRef = DATABASE_REF.child(MAIN_LIST_NODE)
                                      .child(UID)
                                      .child(GMARKERS_NODE)
                                      .child(addressNode);
    }

    @Override
    public LiveData<List<GMarkerMetadata>> getAll()
    {
        return new AllGMarkerMetadataLiveData(userGMarkersRef);
    }

    @Override
    public void insert(GMarkerMetadata gMarkerMetadata)
    {
        String gMarkerKey = userGMarkersRef.push()
                                           .getKey();

        gMarkerMetadata.setId(gMarkerKey);
        Map<String, Object> data = gMarkerMetadata.prepareToTransferToDatabase();

        Map<String, Object> sData = new HashMap<>();
        sData.put(CHILD_ID_NODE, gMarkerKey);
        gMarkersRef.child(gMarkerKey)
                   .updateChildren(data);
        userGMarkersRef.child(gMarkerKey)
                       .updateChildren(sData);
    }

    @Override
    public void update(GMarkerMetadata gMarkerMetadata)
    {

    }

    @Override
    public void delete(GMarkerMetadata gMarkerMetadata)
    {

    }
}
