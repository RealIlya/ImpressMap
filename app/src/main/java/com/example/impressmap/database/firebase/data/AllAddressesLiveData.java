package com.example.impressmap.database.firebase.data;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.ADDRESSES_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllAddressesLiveData extends LiveData<List<Address>>
{
    private final DatabaseReference userAddressesRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            DatabaseReference addressesRef = DATABASE_REF.child(ADDRESSES_NODE);

            List<Address> addressList = new ArrayList<>();
            Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
            while (iterator.hasNext())
            {
                DataSnapshot dataSnapshot = iterator.next();
                boolean hasNext = iterator.hasNext();
                Address value = dataSnapshot.getValue(Address.class);

                addressesRef.child(value.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    Address address = snapshot.getValue(Address.class);
                                    addressList.add(address);

                                    if (!hasNext)
                                    {
                                        setValue(addressList);
                                    }
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

    public AllAddressesLiveData(DatabaseReference userAddressesRef)
    {
        this.userAddressesRef = userAddressesRef;
    }

    @Override
    protected void onActive()
    {
        userAddressesRef.addValueEventListener(listener);
    }

    @Override
    protected void onInactive()
    {
        userAddressesRef.removeEventListener(listener);
    }
}