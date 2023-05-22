package com.example.impressmap.ui.fragment.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.adapter.OnAddressClickListener;
import com.example.impressmap.adapter.PopupAddressesAdapter;
import com.example.impressmap.databinding.PopupAddressesBinding;
import com.example.impressmap.model.data.Address;

import java.util.List;

public class PopupAddressesWindow extends PopupWindow
{
    private final PopupAddressesBinding binding;

    private OnAddressClickListener onAddressClickListener;

    public PopupAddressesWindow(Context context,
                                List<Address> selectedAddresses)
    {
        super(context);
        binding = PopupAddressesBinding.inflate(LayoutInflater.from(context));

        setContentView(binding.getRoot());
        setWidth(450);
        setHeight(600);
        setAttachedInDecor(false);
        setElevation(0);
        setFocusable(true);
        setOverlapAnchor(true);

        RecyclerView recyclerView = binding.addressesRecyclerView;
        PopupAddressesAdapter popupAddressesAdapter = new PopupAddressesAdapter(context,
                selectedAddresses);
        popupAddressesAdapter.setOnAddressClickListener(address ->
        {
            dismiss();
            if (onAddressClickListener != null)
            {
                onAddressClickListener.onAddressClick(address);
            }
        });

        recyclerView.setAdapter(popupAddressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        onAddressClickListener = listener;
    }

    @Override
    public void showAsDropDown(View anchor)
    {
        super.showAsDropDown(anchor, -200, -20);
    }
}
