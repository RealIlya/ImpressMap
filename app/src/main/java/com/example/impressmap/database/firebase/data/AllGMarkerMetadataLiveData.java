package com.example.impressmap.database.firebase.data;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.GMARKERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllGMarkerMetadataLiveData extends LiveData<List<GMarkerMetadata>>
{
    private final DatabaseReference userGMarkerRef;

    private long valueCount = 0;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            DatabaseReference gMarkersRef = DATABASE_REF.child(GMARKERS_NODE);

            List<GMarkerMetadata> gMarkerMetadataList = new ArrayList<>();
            Iterable<DataSnapshot> children = snapshot.getChildren();
            valueCount = children.spliterator().getExactSizeIfKnown(); // something will go wrong
            for (DataSnapshot dataSnapshot : children)
            {
                GMarkerMetadata value = dataSnapshot.getValue(GMarkerMetadata.class);

                gMarkersRef.child(value.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    GMarkerMetadata gMarkerMetadata = snapshot.getValue(GMarkerMetadata.class);
                                    gMarkerMetadataList.add(gMarkerMetadata);
                                    decreaseValue(gMarkerMetadataList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {

                                }
                            });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    public AllGMarkerMetadataLiveData(DatabaseReference userGMarkerRef)
    {
        this.userGMarkerRef = userGMarkerRef;
    }

    @Override
    protected void onActive()
    {
        userGMarkerRef.addValueEventListener(listener);
    }

    @Override
    protected void onInactive()
    {
        userGMarkerRef.removeEventListener(listener);
    }

    private void decreaseValue(List<GMarkerMetadata> gMarkerMetadataList)
    {
        valueCount--;

        if (valueCount == 0)
        {
            setValue(gMarkerMetadataList);
        }
    }
}
