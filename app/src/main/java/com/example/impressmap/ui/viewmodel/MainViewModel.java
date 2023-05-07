package com.example.impressmap.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.User;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel
{
    private MutableLiveData<Integer> mode;
    private MutableLiveData<List<Address>> selectedAddresses;
    private MutableLiveData<String> selectedAddressId;
    private MutableLiveData<User> user;

    public MainViewModel()
    {
        clearCache();
    }

    public LiveData<Integer> getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode.setValue(mode);
    }

    public LiveData<List<Address>> getSelectedAddresses()
    {
        return selectedAddresses;
    }

    public void setSelectedAddresses(List<Address> selectedAddresses)
    {
        this.selectedAddresses.setValue(selectedAddresses);
    }

    public LiveData<String> getSelectedAddressId()
    {
        return selectedAddressId;
    }

    public void setSelectedAddressId(String addressId)
    {
        selectedAddressId.setValue(addressId);
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

    public void clearCache()
    {
        mode = new MutableLiveData<>(-1);
        selectedAddresses = new MutableLiveData<>(new ArrayList<>());
        selectedAddressId = new MutableLiveData<>("");
        user = new MutableLiveData<>();
    }
}
