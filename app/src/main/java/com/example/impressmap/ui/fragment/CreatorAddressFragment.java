package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.bottom.MapInfoFragment.LAT_LNG_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.databinding.FragmentCreatorAddressBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.ui.viewModels.CreatorAddressFragmentViewModel;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.LatLng;

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

        binding.confirmAddressButton.setOnClickListener(v ->
        {
            String title = binding.titleView.getText().toString();
            String desc = binding.descView.getText().toString();
            if (!title.isEmpty() && !desc.isEmpty())
            {
                String[] addressLine = Locations.getAddressLine(getContext(), latLng);
                if (addressLine == null)
                {
                    Toast.makeText(getContext(), "Address does not exist", Toast.LENGTH_LONG)
                         .show();
                    return;
                }

                String country = addressLine[0];
                String city = addressLine[1];
                String state = addressLine[2];

                Address address = new Address();
                address.setDesc(desc);
                address.setFullAddress(String.format("%s %s %s", country, city, state));

                GMarkerMetadata gMarkerMetadata = new GMarkerMetadata();
                gMarkerMetadata.setTitle(title);
                gMarkerMetadata.setLatLng(latLng);
                gMarkerMetadata.setType(GMarkerMetadata.ADDRESS_MARKER);

                viewModel.insert(address, gMarkerMetadata);
            }
        });
    }
}
