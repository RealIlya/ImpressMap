package com.example.impressmap.ui.fragment.creatoraddress;

import static com.example.impressmap.ui.fragment.map.MapFragment.COMMON_MODE;
import static com.example.impressmap.util.Constants.LAT_LNG_KEY;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.LocationsAdapter;
import com.example.impressmap.databinding.FragmentCreatorAddressBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CreatorAddressFragment extends Fragment
{
    private FragmentCreatorAddressBinding binding;
    private CreatorAddressFragmentViewModel viewModel;

    @NonNull
    public static CreatorAddressFragment newInstance(@NonNull LatLng latLng)
    {
        Bundle arguments = new Bundle();
        arguments.putDoubleArray(LAT_LNG_KEY, new double[]{latLng.latitude, latLng.longitude});

        CreatorAddressFragment creatorAddressFragment = new CreatorAddressFragment();
        creatorAddressFragment.setArguments(arguments);
        return creatorAddressFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        new WindowInsetsControllerCompat(requireActivity().getWindow(),
                requireActivity().getWindow().getDecorView()).setAppearanceLightStatusBars(false);
        binding = FragmentCreatorAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(CreatorAddressFragmentViewModel.class);

        double[] rawLatLng = requireArguments().getDoubleArray(LAT_LNG_KEY);
        LatLng latLng = new LatLng(rawLatLng[0], rawLatLng[1]);

        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.toolbar.setTitle(R.string.create_address);

        LocationsAdapter locationsAdapter = new LocationsAdapter(requireContext());

        List<Location> locations = Locations.getFromLatLng(getContext(), latLng);
        if (locations != null)
        {
            locationsAdapter.setAddresses(locations);
            RecyclerView recyclerView = binding.addressesRecyclerView;
            recyclerView.setAdapter(locationsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }

        binding.confirmAddressButton.setOnClickListener(v ->
        {
            Location location = locationsAdapter.getSelectedAddress();
            if (location == null)
            {
                Snackbar.make(requireView(), R.string.no_address_selected, Snackbar.LENGTH_LONG)
                        .show();
                return;
            }

            String title = binding.titleView.getText().toString();
            String desc = binding.descView.getText().toString();
            if (!title.isEmpty())
            {
                location.setPositionLatLng(latLng);
                location.setDesc(desc);

                GMarkerMetadata gMarkerMetadata = new GMarkerMetadata();
                gMarkerMetadata.setTitle(title);
                gMarkerMetadata.setPositionLatLng(latLng);
                gMarkerMetadata.setType(GMarkerMetadata.ADDRESS_MARKER);

                viewModel.insert(location, gMarkerMetadata, () ->
                {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                            MainViewModel.class);
                    mainViewModel.setMode(COMMON_MODE);
                });
            }
            else
            {
                Snackbar.make(requireView(), R.string.field_is_necessary, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Nullable
    @Override
    public Object getEnterTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }

    @Nullable
    @Override
    public Object getExitTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }
}
