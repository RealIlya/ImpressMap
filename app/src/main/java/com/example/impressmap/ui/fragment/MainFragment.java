package com.example.impressmap.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.PostsAdapter;
import com.example.impressmap.adapter.map.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.ui.NavigationDrawer;
import com.example.impressmap.ui.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.bottom.MapInfoFragment;
import com.example.impressmap.ui.viewModels.MainFragmentViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

public class MainFragment extends Fragment
{
    private FragmentMainBinding binding;
    private MainFragmentViewModel viewModel;
    private NavigationDrawer navigationDrawer;
    private PostsBottomSheetBehavior<MaterialCardView> postsSheetBehavior;
    private PostsAdapter postsAdapter;

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
        viewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        setShowMode();
    }

    private void setShowMode()
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
                R.id.map);

        if (supportMapFragment != null)
        {
            supportMapFragment.getMapAsync(new OnMapReadyCallback()
            {
                @SuppressLint("FragmentLiveDataObserve")
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap)
                {
                    GMapAdapter gMapAdapter = new GMapAdapter(getContext(), googleMap);

//        viewModel.getGMarkerMetadataByAddress().observe(this, gMapAdapter::setItems);

                    gMapAdapter.setOnMapLongClickListener(latLng ->
                    {
                        gMapAdapter.setPointer(latLng);
                        gMapAdapter.zoomTo(latLng);

                        MapInfoFragment mapInfoFragment = MapInfoFragment.newInstance(latLng);
                        mapInfoFragment.show(getChildFragmentManager(), null);
                        mapInfoFragment.setOnDismissListener(
                                dialogInterface -> gMapAdapter.removePointer());
                    });
                    gMapAdapter.setOnMapClickListener(latLng -> postsSheetBehavior.hide());
                    gMapAdapter.setOnMarkerClickListener(marker ->
                    {
                        postsAdapter.clear();

                        postsSheetBehavior.showHalf();
                        binding.postsToolbar.setTitle(marker.getTitle());

                        GMarkerMetadata gMarkerMetadata = (GMarkerMetadata) marker.getTag();
                        viewModel.getPostByGMarker(gMarkerMetadata)
                                 .observe(MainFragment.this, postsAdapter::addPost);

                        return true;
                    });
                }
            });
        }

        navigationDrawer = new NavigationDrawer(getContext(), binding.navigationView,
                binding.drawerLayout, requireActivity().getSupportFragmentManager());

        binding.toolbar.setNavigationOnClickListener(view1 -> navigationDrawer.open());

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.framePosts));

        postsAdapter = new PostsAdapter();
        RecyclerView postsRecyclerView = binding.postsRecyclerView;
        postsRecyclerView.setAdapter(postsAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setAddingMode()
    {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
                R.id.map);

        if (supportMapFragment != null)
        {
            supportMapFragment.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap)
                {
                    GMapAdapter gMapAdapter = new GMapAdapter(getContext(), googleMap);

                    gMapAdapter.setOnMapLongClickListener(latLng ->
                    {

                    });
                    gMapAdapter.setOnMapClickListener(latLng ->
                    {
                    });
                    gMapAdapter.setOnMarkerClickListener(marker -> true);
                }
            });
        }
    }
}
