package com.example.impressmap.ui.fragment.map.mode;

import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMapBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.GMarkerWithChildrenMetadata;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.addresses.useraddresses.UserAddressesFragment;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment;
import com.example.impressmap.ui.fragment.bottommarker.behavior.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.bottommarker.posts.PostsFragment;
import com.example.impressmap.ui.fragment.map.MapFragment;
import com.example.impressmap.ui.fragment.map.MapFragmentPopupWindow;
import com.example.impressmap.ui.fragment.map.MapFragmentViewModel;
import com.example.impressmap.ui.fragment.map.NavigationDrawer;
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

    public CommonMode(MapFragment fragment,
                      MapFragmentViewModel viewModel,
                      FragmentMapBinding binding)
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

        activity.getOnBackPressedDispatcher()
                .addCallback(fragment.getViewLifecycleOwner(), new OnBackPressedCallback(true)
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

        mainViewModel.getSelectedAddressId().observe(fragment.getViewLifecycleOwner(), addressId ->
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
                    setEnabled(false);
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

        binding.selectedAddressesFab.setOnClickListener(v ->
        {
            if (mainViewModel.getSelectedAddressesCount() > 0)
            {
                MapFragmentPopupWindow popupWindow = new MapFragmentPopupWindow(fragment);
                popupWindow.setOnAddressClickListener(address ->
                {
                    binding.selectedAddressesFab.setEnabled(false);
                    popupWindow.dismiss();
                    gMapAdapter.animateZoomTo(address,
                            () -> binding.selectedAddressesFab.setEnabled(true));
                });
                popupWindow.showAsDropDown(v, -200, -20);
            }
            else
            {
                String name = UserAddressesFragment.class.getSimpleName();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, UserAddressesFragment.newInstance())
                        .addToBackStack(name)
                        .commit();
            }
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
