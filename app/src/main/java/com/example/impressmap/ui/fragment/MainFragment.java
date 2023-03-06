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

import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapAdapter;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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

        Marker marker = new Marker(binding.mapView);
        marker.setTitle("Ыыы");
        binding.mapView.getOverlays().add(marker);

        com.example.mobv2.ui.view.navigationDrawer.NavigationDrawer navigationDrawer = new NavigationDrawer(
                getContext(), binding.navigationView, binding.drawerLayout,
                getChildFragmentManager());

        binding.toolbar.setNavigationOnClickListener(view1 -> navigationDrawer.open());
    }
}
