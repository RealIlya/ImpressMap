package com.example.impressmap.ui.fragment.map;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.adapter.OnAddressClickListener;
import com.example.impressmap.adapter.PopupAddressesAdapter;
import com.example.impressmap.databinding.PopupAddressesBinding;
import com.example.impressmap.ui.activity.main.MainViewModel;

public class PopupWindow extends android.widget.PopupWindow
{
    private final PopupAddressesAdapter adapter;

    public PopupWindow(MapFragment fragment)
    {
        PopupAddressesBinding binding = PopupAddressesBinding.inflate(
                LayoutInflater.from(fragment.requireContext()));
        setContentView(binding.getRoot());
        setWidth(450);
        setHeight(450);
        setFocusable(true);
        setOverlapAnchor(true);

        MainViewModel mainViewModel = new ViewModelProvider(fragment.requireActivity()).get(
                MainViewModel.class);

        RecyclerView recyclerView = binding.addressesRecyclerView;
        adapter = new PopupAddressesAdapter(fragment.requireContext(),
                mainViewModel.getSelectedAddresses().getValue());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.requireContext()));
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        if (adapter != null)
        {
            adapter.setOnAddressClickListener(listener);
        }
    }
}
