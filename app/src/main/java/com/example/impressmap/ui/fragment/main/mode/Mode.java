package com.example.impressmap.ui.fragment.main.mode;

import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.adapter.map.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.MainFragmentViewModel;

public abstract class Mode
{
    protected MainFragment fragment;
    protected MainFragmentViewModel viewModel;
    protected FragmentMainBinding binding;

    public Mode(MainFragment fragment,
                FragmentMainBinding binding)
    {
        this.fragment = fragment;
        this.binding = binding;
        this.viewModel = new ViewModelProvider(fragment).get(MainFragmentViewModel.class);
    }

    public abstract void switchOn(GMapAdapter gMapAdapter);
}
