package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.bottom.MapInfoFragment.LAT_LNG_KEY;
import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.databinding.FragmentCreatorCommonMarkerBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.ui.viewModels.MainFragmentViewModel;
import com.google.android.gms.maps.model.LatLng;

public class CreatorCommonMarkerFragment extends Fragment
{
    private FragmentCreatorCommonMarkerBinding binding;
    private MainFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentCreatorCommonMarkerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        double[] rawLatLng = requireArguments().getDoubleArray(LAT_LNG_KEY);
        LatLng latLng = new LatLng(rawLatLng[0], rawLatLng[1]);

        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager()
                                      .beginTransaction()
                                      .remove(CreatorCommonMarkerFragment.this)
                                      .commit());
        binding.confirmMarkerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = binding.markerTitleView.getText()
                                                      .toString();
                String text = binding.markerTextView.getText()
                                                    .toString();
                if (!title.isEmpty() && !text.isEmpty())
                {
                    GMarkerMetadata gMarkerMetadata = new GMarkerMetadata();
                    gMarkerMetadata.setTitle(title);
                    gMarkerMetadata.setLatLng(latLng);
                    gMarkerMetadata.setType(GMarkerMetadata.COMMON_MARKER);

                    OwnerUser ownerUser = new OwnerUser();
                    ownerUser.setId(UID);
                    ownerUser.setFullName(AUTH.getCurrentUser()
                                              .getEmail());

                    Post post = new Post();
                    post.setText(text);
                    post.setOwnerUser(ownerUser);
                    viewModel.insertCommonMarker("", gMarkerMetadata, post);
                }
            }
        });
    }

    public static CreatorCommonMarkerFragment newInstance(LatLng latLng)
    {
        Bundle arguments = new Bundle();
        arguments.putDoubleArray(LAT_LNG_KEY, new double[]{latLng.latitude, latLng.longitude});

        CreatorCommonMarkerFragment creatorCommonMarkerFragment = new CreatorCommonMarkerFragment();
        creatorCommonMarkerFragment.setArguments(arguments);
        return creatorCommonMarkerFragment;
    }
}
