package com.example.impressmap.ui.fragment.map.mode;

import androidx.annotation.NonNull;

import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMapBinding;
import com.example.impressmap.ui.fragment.map.MapFragment;
import com.example.impressmap.ui.fragment.map.MapFragmentViewModel;

public abstract class Mode
{
    protected MapFragment fragment;
    protected MapFragmentViewModel viewModel;
    protected FragmentMapBinding binding;

    public Mode(@NonNull MapFragment fragment,
                @NonNull MapFragmentViewModel viewModel,
                FragmentMapBinding binding)
    {
        this.fragment = fragment;
        this.viewModel = viewModel;
        this.binding = binding;
    }

    public abstract void switchOn(GMapAdapter gMapAdapter);
}
