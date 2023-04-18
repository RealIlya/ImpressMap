package com.example.impressmap.ui.fragment.bottom;

import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentMapInfoBinding;
import com.example.impressmap.ui.fragment.CreatorCommonMarkerFragment;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MapInfoFragment extends BottomSheetDialogFragment
{
    public static final String LAT_LNG_KEY = "LAT_LNG_KEY";

    private FragmentMapInfoBinding binding;

    private DialogInterface.OnDismissListener onDismissListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentMapInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        requireDialog().setOnDismissListener(onDismissListener);

        double[] rawLatLng = requireArguments().getDoubleArray(LAT_LNG_KEY);
        LatLng latLng = new LatLng(rawLatLng[0], rawLatLng[1]);

        Address address = Locations.getFromLocation(getContext(), latLng);

        if (address != null)
        {
            binding.toolbar.setTitle(address.getAddressLine(0));
        }
        binding.toolbar.setSubtitle(latLng.latitude + " : " + latLng.longitude);

        binding.createSubAddressMarkerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();

                requireActivity().getSupportFragmentManager()
                                 .beginTransaction()
                                 .add(R.id.container, CreatorCommonMarkerFragment.newInstance(latLng))
                                 .commit();
            }
        });
    }

    public static MapInfoFragment newInstance(LatLng latLng)
    {
        Bundle arguments = new Bundle();
        arguments.putDoubleArray(LAT_LNG_KEY, new double[]{latLng.latitude, latLng.longitude});

        MapInfoFragment mapInfoFragment = new MapInfoFragment();
        mapInfoFragment.setArguments(arguments);
        return mapInfoFragment;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        onDismissListener = listener;
    }
}
