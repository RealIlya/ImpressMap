package com.example.impressmap.database.firebase.data;

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
    private DatabaseReference addressesRef;

    /*
        Это все выглядит странно. Зачем делать специальную реализацию LiveData внутри которой
        будет располагться какая то логика работы с данными. Сама по себе LiveData это просто
        реализация паттерна Observable и она должна выполнять какую то одну задачу, а не лазить внутри себя в БД

        По идее такая логика должна находится в Storage внутри дата слоя, но никак ни в LiveData.
        А уже этот Storage будет возвразать класическую LiveData в которую будет передавать данные.

        Относится ко всем файлам в этом пакете.
     */
    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            List<Address> addressList = new ArrayList<>();
            Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

            if (!iterator.hasNext())
            {
                setValue(addressList);
            }

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

    public AllAddressesLiveData(DatabaseReference addressesRef)
    {
        this.addressesRef = addressesRef;
    }

    @Override
    protected void onActive()
    {
        addressesRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    protected void onInactive()
    {
        addressesRef.removeEventListener(listener);
    }
}