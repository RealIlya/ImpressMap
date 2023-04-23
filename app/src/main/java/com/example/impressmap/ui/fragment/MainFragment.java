package com.example.impressmap.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.PostsAdapter;
import com.example.impressmap.adapter.map.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.ui.NavigationDrawer;
import com.example.impressmap.ui.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.bottom.MapInfoFragment;
import com.example.impressmap.ui.viewModels.MainFragmentViewModel;
import com.example.impressmap.ui.viewModels.MainViewModel;
import com.example.impressmap.util.mode.Mode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MainFragment extends Fragment
{
    public static final int COMMON_MODE = 0, ADDING_MODE = 1;

    private FragmentMainBinding binding;
    private MainFragmentViewModel viewModel;
    private NavigationDrawer navigationDrawer;
    private PostsBottomSheetBehavior<MaterialCardView> postsSheetBehavior;
    private PostsAdapter postsAdapter;

    @NonNull
    public static MainFragment newInstance()
    {
        return new MainFragment();
    }

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

        requireActivity().getOnBackPressedDispatcher()
                         .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
                         {
                             @Override
                             public void handleOnBackPressed()
                             {
                                 FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                 if (fragmentManager.getBackStackEntryCount() > 0)
                                 {
                                     fragmentManager.popBackStack();
                                 }
                                 else
                                 {
                                     setEnabled(false);
                                     requireActivity().onBackPressed();
                                 }
                             }
                         });

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

                    gMapAdapter.removeListeners();

                    MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                            MainViewModel.class);
                    mainViewModel.getMode()
                                 .observe(getViewLifecycleOwner(),
                                         mode -> switchMode(gMapAdapter, mode));
                }
            });
        }

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.framePosts));
    }

    private void switchMode(GMapAdapter gMapAdapter,
                            int mode)
    {
        Mode modeClass;

        switch (mode)
        {
            case ADDING_MODE:
                modeClass = new AddingMode();
                break;
            default:
                modeClass = new CommonMode();
                break;
        }

        modeClass.switchOn(gMapAdapter);
    }

    private class CommonMode implements Mode
    {
        @Override
        public void switchOn(GMapAdapter gMapAdapter)
        {
            MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                    MainViewModel.class);

            mainViewModel.getSelectedAddresses().observe(getViewLifecycleOwner(), addressList ->
            {
                // not optimized
                gMapAdapter.clearMap();
                if (!addressList.isEmpty())
                {
                    for (Address address : addressList)
                    {
                        viewModel.getGMarkerMetadataByAddress(address)
                                 .observe(getViewLifecycleOwner(), gMapAdapter::addZone);
                    }
                }
            });

            mainViewModel.getSelectedAddressId().observe(getViewLifecycleOwner(), addressId ->
            {
                if (addressId.isEmpty())
                {
                    MenuItem menuItem = binding.toolbar.getMenu()
                                                       .findItem(R.id.deselect_circle_menu);
                    if (menuItem != null)
                    {
                        menuItem.setVisible(false);
                    }
                    gMapAdapter.deselectLastCircle();
                }
                else
                {
                    MenuItem menuItem = binding.toolbar.getMenu()
                                                       .findItem(R.id.deselect_circle_menu);
                    if (menuItem != null)
                    {
                        menuItem.setVisible(true);
                    }
                }
            });

            gMapAdapter.zoomTo(new LatLng(54.849540, 83.106605));

            gMapAdapter.setOnMapLongClickListener(latLng ->
            {
                if (!mainViewModel.getSelectedAddressId().getValue().isEmpty())
                {
                    gMapAdapter.setPointer(latLng);
                    gMapAdapter.zoomTo(latLng);

                    MapInfoFragment mapInfoFragment = MapInfoFragment.newInstance(latLng);
                    String name = MapInfoFragment.class.getSimpleName();
                    mapInfoFragment.show(requireActivity().getSupportFragmentManager(), name);
                    mapInfoFragment.setOnDismissListener(
                            dialogInterface -> gMapAdapter.removePointer());
                }
            });
            gMapAdapter.setOnMapClickListener(latLng -> postsSheetBehavior.hide());
            gMapAdapter.setOnMarkerClickListener(marker ->
            {
                postsAdapter.clear();

                postsSheetBehavior.showHalf();
                binding.postsToolbar.setTitle(marker.getTitle());

                GMarkerMetadata gMarkerMetadata = ((GMarker) marker.getTag()).getGMarkerMetadata();
                if (gMarkerMetadata.getType() == GMarkerMetadata.COMMON_MARKER)
                {
                    viewModel.getPostByGMarker(gMarkerMetadata)
                             .observe(getViewLifecycleOwner(), postsAdapter::addPost);
                }
                else if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
                {
                    List<GMarker> gMarkers = gMapAdapter.getLastSelectedCircleGMarkers();
                    if (gMarkers != null)
                    {
                        for (GMarker gMarker : gMarkers)
                        {
                            viewModel.getPostByGMarker(gMarker.getGMarkerMetadata())
                                     .observe(getViewLifecycleOwner(), postsAdapter::addPost);
                        }
                    }
                }

                return true;
            });
            gMapAdapter.setOnCircleClickListener(circle ->
            {
                GCircleMeta gCircleMeta = ((GCircle) circle.getTag()).getGCircleMeta();
                mainViewModel.setSelectedAddressId(gCircleMeta.getAddressId());
            });

            navigationDrawer = new NavigationDrawer(getContext(), binding.navigationView,
                    binding.drawerLayout, requireActivity().getSupportFragmentManager());

            binding.toolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
            binding.toolbar.setNavigationOnClickListener(v -> navigationDrawer.open());
            binding.toolbar.inflateMenu(R.menu.menu_map);
            binding.toolbar.getMenu()
                           .findItem(R.id.deselect_circle_menu)
                           .setOnMenuItemClickListener(menuItem ->
                           {
                               mainViewModel.setSelectedAddressId("");
                               return true;
                           });
            binding.toolbar.getMenu().findItem(R.id.deselect_circle_menu).setVisible(false);

            postsAdapter = new PostsAdapter();
            RecyclerView recyclerView = binding.postsRecyclerView;
            recyclerView.setAdapter(postsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private class AddingMode implements Mode
    {
        @Override
        public void switchOn(GMapAdapter gMapAdapter)
        {
            MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                    MainViewModel.class);

            gMapAdapter.setOnMapClickListener(latLng ->
            {
                gMapAdapter.setPointer(latLng);
                gMapAdapter.zoomTo(latLng);

                CreatorAddressFragment fragment = CreatorAddressFragment.newInstance(latLng);
                String name = CreatorAddressFragment.class.getSimpleName();
                requireActivity().getSupportFragmentManager()
                                 .beginTransaction()
                                 .replace(R.id.container, fragment)
                                 .addToBackStack(name)
                                 .commit();
            });

            binding.toolbar.setNavigationIcon(R.drawable.ic_arrow);
            binding.toolbar.setNavigationOnClickListener(v -> mainViewModel.setMode(COMMON_MODE));
            binding.toolbar.getMenu().clear();
        }
    }
}
