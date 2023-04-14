package com.example.impressmap.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.impressmap.R;
import com.example.impressmap.adapter.Adapter;
import com.example.impressmap.adapter.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.util.NavigationDrawer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

public class MainFragment extends Fragment implements OnMapReadyCallback
{
    private FragmentMainBinding binding;
    private BottomSheetBehavior<MaterialCardView> bottomSheetBehavior;

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

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
                R.id.map);

        if (supportMapFragment != null)
        {
            supportMapFragment.getMapAsync(this);
        }

        NavigationDrawer navigationDrawer = new NavigationDrawer(
                getContext(), binding.navigationView, binding.drawerLayout,
                getChildFragmentManager());

        binding.toolbar.setNavigationOnClickListener(view1 -> navigationDrawer.open());

        bottomSheetBehavior = BottomSheetBehavior.from(binding.framePosts);
        bottomSheetBehavior.setHalfExpandedRatio(0.4f);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            private boolean isCollapsing = false;
            private boolean isExpanding = false;
            private boolean isSettling = false;
            private float oldOffset = 0f;

            private void setDefaultState()
            {
                isCollapsing = false;
                isExpanding = false;
                isSettling = false;
            }

            @Override
            public void onStateChanged(@NonNull View bottomSheet,
                                       int newState)
            {
                MaterialCardView framePosts = binding.framePosts;
                AppBarLayout postsAppBar = binding.postsAppBar;

                switch (newState)
                {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        setDefaultState();
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        isSettling = true;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet,
                                float slideOffset)
            {
                isExpanding = oldOffset < slideOffset;
                isCollapsing = oldOffset > slideOffset;
                if (slideOffset < 1 && slideOffset >= 0.5 && isCollapsing || slideOffset < 0.4 && slideOffset >= 0 && isExpanding)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }

                oldOffset = slideOffset;

                // fix
                if (slideOffset < 0 && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

               /* AppBarLayout postsAppBar = binding.postsAppBar;
                postsAppBar.animate()
                           .translationY(((float) -Math.pow(postsAppBar.getHeight(),
                                   1 - slideOffset / 1.5)))
                           .setDuration(0)
                           .start();*/
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        GMapAdapter gMapAdapter = new GMapAdapter(getContext(), googleMap);

        // temporary
/*        for (GMarkerMetadata gMarkerMetadata : GMarkersRepository.getGMarkerMetadata())
        {
            gMapAdapter.addMarker(gMarkerMetadata);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                GMarkersRepository.getGMarkerMetadata().get(0).getLatLng(), Adapter.ZOOM));*/

        gMapAdapter.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        gMapAdapter.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                binding.postsToolbar.setTitle(marker.getTitle());
                return true;
            }
        });
    }
}
