package com.example.impressmap.ui.fragment.bottom;

import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentBottomSheetBinding;
import com.example.impressmap.ui.fragment.CreatorMarkerFragment;
import com.example.impressmap.ui.viewModels.MainFragmentViewModel;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment
{
    public static final String LAT_LNG_KEY = "LAT_LNG_KEY";

    private FragmentBottomSheetBinding binding;
    private MainFragmentViewModel viewModel;

    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnCancelListener onCancelListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        requireDialog().setOnDismissListener(onDismissListener);
        requireDialog().setOnCancelListener(onCancelListener);

        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

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
                                 .add(R.id.container, CreatorMarkerFragment.newInstance(latLng))
                                 .commit();
            }
        });
    }

    public static BottomSheetFragment newInstance(LatLng latLng)
    {
        Bundle arguments = new Bundle();
        arguments.putDoubleArray(LAT_LNG_KEY, new double[]{latLng.latitude, latLng.longitude});

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setArguments(arguments);
        return bottomSheetFragment;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        onDismissListener = listener;
    }

    public void setOnCanselListener(DialogInterface.OnCancelListener listener)
    {
        onCancelListener = listener;
    }
}
