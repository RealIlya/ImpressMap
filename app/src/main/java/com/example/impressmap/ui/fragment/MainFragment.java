package com.example.impressmap.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.mobv2.ui.view.navigationDrawer.NavigationDrawer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

public class MainFragment extends Fragment
{
    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().beginTransaction()
                                 .replace(binding.map.getId(), new MapFragment())
                                 .commit();

        com.example.mobv2.ui.view.navigationDrawer.NavigationDrawer navigationDrawer = new NavigationDrawer(
                getContext(), binding.navigationView, binding.drawerLayout,
                getChildFragmentManager());

        binding.toolbar.setNavigationOnClickListener(view1 -> navigationDrawer.open());


        BottomSheetBehavior<MaterialCardView> bottomSheetBehavior = BottomSheetBehavior.from(binding.framePosts);
        bottomSheetBehavior.setPeekHeight(100, true);
        bottomSheetBehavior.setHalfExpandedRatio(0.1f);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet,
                                       int newState)
            {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet,
                                float slideOffset)
            {

            }
        });
    }
}
