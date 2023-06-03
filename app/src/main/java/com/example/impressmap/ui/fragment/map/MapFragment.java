package com.example.impressmap.ui.fragment.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMapBinding;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.map.mode.AddingMode;
import com.example.impressmap.ui.fragment.map.mode.CommonMode;
import com.example.impressmap.ui.fragment.map.mode.Mode;
import com.example.impressmap.util.SwitchableMode;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapFragment extends Fragment implements SwitchableMode, ActivityResultCallback<Boolean>
{
    public static final int COMMON_MODE = 0, ADDING_MODE = 1;
    private final ActivityResultLauncher<String> permission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), this);
    private FragmentMapBinding binding;
    private GMapAdapter gMapAdapter;
    private MapFragmentViewModel viewModel;

    @NonNull
    public static MapFragment newInstance()
    {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(MapFragmentViewModel.class);

        requireActivity().getOnBackPressedDispatcher()
                         .addCallback(getViewLifecycleOwner(),
                                 new MapFragmentOnBackPressedCallback(this));

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
                R.id.map);

        if (supportMapFragment != null)
        {
            supportMapFragment.getMapAsync(googleMap ->
            {
                gMapAdapter = new GMapAdapter(getContext(), googleMap, requireActivity());
                gMapAdapter.removeListeners();

                permission.launch(Manifest.permission.ACCESS_FINE_LOCATION);

                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode)
                {
                    case Configuration.UI_MODE_NIGHT_NO:
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),
                                R.raw.map_day));
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),
                                R.raw.map_night));
                        break;
                }

                MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                        MainViewModel.class);
                mainViewModel.getMode().observe(getViewLifecycleOwner(), this::switchMode);
            });
        }
    }

    public void switchMode(int mode)
    {
        Mode modeClass;

        if (mode == ADDING_MODE)
        {
            modeClass = new AddingMode(this, viewModel, binding);
        }
        else
        {
            modeClass = new CommonMode(this, viewModel, binding);
        }

        modeClass.switchOn(gMapAdapter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getViewModelStore().clear();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(Boolean result)
    {
        if (result)
        {
            gMapAdapter.setMyLocationEnabled(true);
            binding.myLocationFab.setOnClickListener(v -> gMapAdapter.animateZoomToMyLocation(() ->
            {
                Snackbar.make(v, R.string.location_loading, Snackbar.LENGTH_SHORT)
                        .show();
            }));
        }
        else
        {
            binding.myLocationFab.setOnClickListener(
                    v -> Snackbar.make(v, R.string.location_permission,
                            Snackbar.LENGTH_LONG).show());
        }
    }

    @Nullable
    @Override
    public Object getExitTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }
}
