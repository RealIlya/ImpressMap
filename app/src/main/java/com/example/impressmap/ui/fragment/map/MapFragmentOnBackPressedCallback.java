package com.example.impressmap.ui.fragment.map;

import static com.example.impressmap.ui.fragment.map.MapFragment.ADDING_MODE;
import static com.example.impressmap.ui.fragment.map.MapFragment.COMMON_MODE;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.ui.activity.main.MainViewModel;

public class MapFragmentOnBackPressedCallback extends OnBackPressedCallback
{
    private final MapFragment fragment;

    public MapFragmentOnBackPressedCallback(MapFragment fragment)
    {
        super(true);

        this.fragment = fragment;
    }

    @Override
    public void handleOnBackPressed()
    {
        MainViewModel mainViewModel = new ViewModelProvider(fragment.requireActivity()).get(
                MainViewModel.class);
        FragmentManager fragmentManager = fragment.requireActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0)
        {
            fragmentManager.popBackStack();
        }
        else
        {
            Integer mode = mainViewModel.getMode().getValue();
            if (mode != null && mode == ADDING_MODE)
            {
                mainViewModel.setMode(COMMON_MODE);
            }
            else
            {
                setEnabled(false);
                fragment.requireActivity().onBackPressed();
            }
        }
    }
}
