package com.example.impressmap.ui.fragment.main.mode;

import androidx.annotation.NonNull;

import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.MainFragmentViewModel;

public abstract class Mode
{
    protected MainFragment fragment;
    protected MainFragmentViewModel viewModel;
    protected FragmentMainBinding binding;

    public Mode(@NonNull MainFragment fragment,
                @NonNull MainFragmentViewModel viewModel,
                FragmentMainBinding binding)
    {
        this.fragment = fragment;
        this.viewModel = viewModel;
        this.binding = binding;
    }

    public abstract void switchOn(GMapAdapter gMapAdapter);
}
