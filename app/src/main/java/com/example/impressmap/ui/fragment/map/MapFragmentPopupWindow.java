package com.example.impressmap.ui.fragment.map;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.adapter.OnAddressClickListener;
import com.example.impressmap.adapter.PopupAddressesAdapter;
import com.example.impressmap.databinding.PopupAddressesBinding;
import com.example.impressmap.ui.activity.main.MainViewModel;

public class MapFragmentPopupWindow extends android.widget.PopupWindow
{
    private final PopupAddressesAdapter popupAddressesAdapter;

    public MapFragmentPopupWindow(MapFragment fragment)
    {
        PopupAddressesBinding popupAddressesBinding = PopupAddressesBinding.inflate(
                LayoutInflater.from(fragment.requireContext()));
        setContentView(popupAddressesBinding.getRoot());
        setWidth(450);
        setHeight(450);
        setFocusable(true);
        setOverlapAnchor(true);

        MainViewModel mainViewModel = new ViewModelProvider(fragment.requireActivity()).get(
                MainViewModel.class);

        RecyclerView recyclerView = popupAddressesBinding.addressesRecyclerView;
        popupAddressesAdapter = new PopupAddressesAdapter(fragment.requireContext(),
                mainViewModel.getSelectedAddresses().getValue());

        recyclerView.setAdapter(popupAddressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.requireContext()));
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        if (popupAddressesAdapter == null)
        {
            return;
        }
        popupAddressesAdapter.setOnAddressClickListener(listener);
    }
}
