package com.example.impressmap.ui.activity.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel
{
    private final UserCase userCase;
    private MutableLiveData<Integer> mode;
    private MutableLiveData<List<Address>> selectedAddresses;
    private MutableLiveData<String> selectedAddressId;

    public MainViewModel()
    {
        clearCache();
        userCase = new UserCase();
    }

    public LiveData<Integer> getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode.setValue(mode);
    }

    public int getSelectedAddressesCount()
    {
        List<Address> value = selectedAddresses.getValue();
        if (value != null)
        {
            return value.size();
        }
        return 0;
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
        return userCase.getUser();
    }

    public void setUser(User user,
                        SuccessCallback successCallback)
    {
        userCase.update(user, successCallback);
    }

    public void clearCache()
    {
        mode = new MutableLiveData<>(-1);
        selectedAddresses = new MutableLiveData<>(new ArrayList<>());
        selectedAddressId = new MutableLiveData<>("");
    }
}
