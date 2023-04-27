package com.example.impressmap.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.User;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel
{
    public static final int MAX_ADDRESSES_COUNT = 5;
    private final MutableLiveData<Integer> mode = new MutableLiveData<>(-1);
    private final MutableLiveData<List<Address>> addresses = new MutableLiveData<>();
    private final MutableLiveData<List<Address>> selectedAddresses = new MutableLiveData<>();
    private final MutableLiveData<String> selectedAddressId = new MutableLiveData<>("");
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private Circle lastSelectedCircle = null;
    private Marker lastSelectedMarker = null;

    public MainViewModel()
    {
        selectedAddresses.setValue(new ArrayList<>());
    }

    public LiveData<Integer> getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode.setValue(mode);
    }

    public LiveData<List<Address>> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(@NonNull List<Address> addresses)
    {
        if (!addresses.equals(this.addresses.getValue()))
        {
            this.addresses.setValue(addresses);
        }
    }

    public LiveData<List<Address>> getSelectedAddresses()
    {
        return selectedAddresses;
    }

    public LiveData<String> getSelectedAddressId()
    {
        return selectedAddressId;
    }

    public void setSelectedAddressId(String addressId)
    {
        selectedAddressId.setValue(addressId);
    }

    public void switchSelectionAddress(Address address,
                                       OnMaxAddressesCallback onMaxAddressesCallback)
    {
        List<Address> addresses = selectedAddresses.getValue();

        if (addresses == null)
        {
            return;
        }
        if (addresses.size() >= MAX_ADDRESSES_COUNT && !address.isSelected())
        {
            onMaxAddressesCallback.onSet();
            return;
        }

        if (address.isSelected())
        {
            addresses.remove(address);
            address.setSelected(false);
        }
        else
        {
            addresses.add(address);
            address.setSelected(true);
        }

        selectedAddresses.setValue(addresses);
    }

    public LiveData<User> getUser()
    {
        return user;
    }

    public void setUser(@NonNull User user)
    {
        if (!user.equals(this.user.getValue()))
        {
            this.user.setValue(user);
        }
    }

    public Circle getLastSelectedCircle()
    {
        return lastSelectedCircle;
    }

    public void setLastSelectedCircle(Circle lastSelectedCircle)
    {
        this.lastSelectedCircle = lastSelectedCircle;
    }

    public Marker getLastSelectedMarker()
    {
        return lastSelectedMarker;
    }

    public void setLastSelectedMarker(Marker lastSelectedMarker)
    {
        this.lastSelectedMarker = lastSelectedMarker;
    }

    public interface OnMaxAddressesCallback
    {
        void onSet();
    }
}
