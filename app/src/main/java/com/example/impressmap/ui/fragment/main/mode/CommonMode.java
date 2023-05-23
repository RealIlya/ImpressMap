package com.example.impressmap.ui.fragment.main.mode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.PopupAddressesAdapter;
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.databinding.PopupAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.GMarkerWithChildrenMetadata;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment;
import com.example.impressmap.ui.fragment.bottommarker.behavior.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.bottommarker.posts.PostsFragment;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.fragment.main.MainFragmentViewModel;
import com.example.impressmap.ui.fragment.main.NavigationDrawer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CommonMode extends Mode
{
    private final FragmentActivity activity;
    private final MainViewModel mainViewModel;
    private NavigationDrawer navigationDrawer;
    private PostsBottomSheetBehavior<MaterialCardView> postsSheetBehavior;

    public CommonMode(MainFragment fragment,
                      MainFragmentViewModel viewModel,
                      FragmentMainBinding binding)
    {
        super(fragment, viewModel, binding);
        activity = fragment.requireActivity();
        mainViewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    }

    @Override
    public void switchOn(GMapAdapter gMapAdapter)
    {
        navigationDrawer = new NavigationDrawer(activity, binding.navigationView,
                binding.drawerLayout, activity.getSupportFragmentManager());

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.bottomView), activity);

        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                0
        );
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            gMapAdapter.setMyLocationEnabled(true);
        }

        activity.getOnBackPressedDispatcher().addCallback(fragment, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (navigationDrawer.isOpen())
                {
                    navigationDrawer.close();
                }
            }
        });

        /*gMapAdapter.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                gMapAdapter.animateZoomTo(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });*/

        Toolbar toolbar = binding.toolbar;

        LiveData<List<Address>> addressesLiveData = mainViewModel.getSelectedAddresses();
        if (!addressesLiveData.hasActiveObservers())
        {
            addressesLiveData.observe(activity, addressList ->
            {
                gMapAdapter.clearMap();
                if (!addressList.isEmpty())
                {
                    for (Address address : addressList)
                    {
                        viewModel.getGMarkerMetadataByAddress(address)
                                 .observeForever(gMapAdapter::addZone);
                    }
                }
            });
        }

        mainViewModel.getSelectedAddressId().observe(fragment, addressId ->
        {
            MenuItem item = toolbar.getMenu().findItem(R.id.deselect_circle_item);

            if (item != null)
            {
                if (addressId.isEmpty())
                {
                    item.setVisible(false);
                    gMapAdapter.deselectGCircle();
                }
                else
                {
                    item.setVisible(true);
                }
            }
        });

        gMapAdapter.setOnMapLongClickListener(latLng ->
        {
            gMapAdapter.setPointer(latLng);

            boolean inZone = gMapAdapter.inSelectedGCircle(latLng);
            MapInfoFragment mapInfoFragment = MapInfoFragment.newInstance(latLng, inZone);
            String name = MapInfoFragment.class.getSimpleName();
            mapInfoFragment.setOnDismissListener(dialogInterface -> gMapAdapter.removePointer());
            gMapAdapter.animateZoomTo(latLng,
                    () -> mapInfoFragment.show(activity.getSupportFragmentManager(), name));
        });

        gMapAdapter.setOnMapClickListener(latLng -> postsSheetBehavior.hide());
        gMapAdapter.setOnMarkerClickListener(marker ->
        {
            postsSheetBehavior.showHalf();
            GMarker markerTag = (GMarker) marker.getTag();
            if (markerTag == null)
            {
                return false;
            }
            GMarkerMetadata gMarkerMetadata = markerTag.getGMarkerMetadata();

            FragmentTransaction transaction = fragment.getChildFragmentManager().beginTransaction();

            PostsFragment postsFragment;
            if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                GMarkerWithChildrenMetadata withChildrenMetadata = GMarkerWithChildrenMetadata.convert(
                        gMarkerMetadata);
                List<GMarkerMetadata> gMarkerMetadataList = new ArrayList<>();

                List<GMarker> selectedGCircleGMarkers = gMapAdapter.getSelectedGCircleGMarkers();
                if (selectedGCircleGMarkers == null)
                {
                    return false;
                }

                for (GMarker gMarker : selectedGCircleGMarkers)
                {
                    gMarkerMetadataList.add(gMarker.getGMarkerMetadata());
                }

                withChildrenMetadata.addGMarkersMetadata(gMarkerMetadataList);

                postsFragment = PostsFragment.newInstance(withChildrenMetadata);
            }
            else
            {
                postsFragment = PostsFragment.newInstance(gMarkerMetadata);
            }

            postsFragment.setOnDeselectItemClickListener(
                    view -> postsSheetBehavior.hide(gMapAdapter::deselectGMarker));
            postsFragment.setOnBackPressedCallback(new OnBackPressedCallback(true)
            {
                @Override
                public void handleOnBackPressed()
                {
                    postsSheetBehavior.hide(gMapAdapter::deselectGMarker);
                }
            });
            transaction.replace(R.id.bottom_container, postsFragment);

            String name = PostsFragment.class.getSimpleName();
            transaction.addToBackStack(name).commit();

            return true;
        });
        gMapAdapter.setOnCircleClickListener(circle ->
        {
            GCircle circleTag = (GCircle) circle.getTag();
            if (circleTag == null)
            {
                return;
            }
            GCircleMeta gCircleMeta = circleTag.getGCircleMeta();
            postsSheetBehavior.hide(
                    () -> mainViewModel.setSelectedAddressId(gCircleMeta.getAddressId()));
        });

        toolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
        toolbar.setNavigationOnClickListener(v -> navigationDrawer.open());
        toolbar.inflateMenu(R.menu.menu_map);

        toolbar.getMenu().findItem(R.id.deselect_circle_item).setOnMenuItemClickListener(item ->
        {
            postsSheetBehavior.hide(() -> mainViewModel.setSelectedAddressId(""));
            return true;
        });
        toolbar.getMenu().findItem(R.id.deselect_circle_item).setVisible(false);

        binding.selectedAddressesFab.setOnClickListener(v ->
        {
            PopupAddressesBinding popupAddressesBinding = PopupAddressesBinding.inflate(
                    LayoutInflater.from(activity));

            PopupWindow popupWindow = new PopupWindow(popupAddressesBinding.getRoot(), 450, 600,
                    true);
            popupWindow.setOverlapAnchor(true);

            RecyclerView recyclerView = popupAddressesBinding.addressesRecyclerView;
            PopupAddressesAdapter popupAddressesAdapter = new PopupAddressesAdapter(
                    fragment.requireContext(), mainViewModel.getSelectedAddresses().getValue());
            popupAddressesAdapter.setOnAddressClickListener(address ->
            {
                binding.selectedAddressesFab.setEnabled(false);
                popupWindow.dismiss();
                gMapAdapter.animateZoomTo(address,
                        () -> binding.selectedAddressesFab.setEnabled(true));
            });

            recyclerView.setAdapter(popupAddressesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            int itemCount = popupAddressesAdapter.getItemCount();
            if (itemCount > 0)
            {
                popupWindow.showAsDropDown(v, -200, -20);
            }
        });
        binding.myLocationFab.setOnClickListener(v ->
        {
            gMapAdapter.animateZoomToMyLocation();
        });

        postsSheetBehavior.setAnimation(new PostsBottomSheetBehavior.Animation()
        {
            @Override
            public void onStateChanged(int newState)
            {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    binding.bottomView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(float slideOffset)
            {
            }
        });
    }
}
