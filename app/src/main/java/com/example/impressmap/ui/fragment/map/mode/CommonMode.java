package com.example.impressmap.ui.fragment.map.mode;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMapBinding;
import com.example.impressmap.databinding.NavHeaderMainBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.addresses.AddressesFragment;
import com.example.impressmap.ui.fragment.addresses.useraddresses.UserAddressesFragment;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment;
import com.example.impressmap.ui.fragment.bottommarker.behavior.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.bottommarker.posts.PostsFragment;
import com.example.impressmap.ui.fragment.map.MapFragment;
import com.example.impressmap.ui.fragment.map.MapFragmentViewModel;
import com.example.impressmap.ui.fragment.map.PopupWindow;
import com.example.impressmap.ui.fragment.profile.ProfileFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * <p>Mode for MapFragment</p>
 * <p>Works when user is using a map</p>
 */
public class CommonMode extends Mode implements NavigationView.OnNavigationItemSelectedListener
{
    private final FragmentActivity activity;
    private final MainViewModel mainViewModel;
    private PostsBottomSheetBehavior<MaterialCardView> sheetBehavior;
    private OnBackPressedCallback onBackPressedCallback;

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
        sheetBehavior = new PostsBottomSheetBehavior<>(BottomSheetBehavior.from(binding.bottomView),
                activity);

        onBackPressedCallback = new OnBackPressedCallback(false)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (binding.drawerLayout.isOpen())
                {
                    binding.drawerLayout.close();
                }
            }
        };
        initNavigationDrawer();
        activity.getOnBackPressedDispatcher()
                .addCallback(fragment.getViewLifecycleOwner(), onBackPressedCallback);

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
            MenuItem item = binding.toolbar.getMenu().findItem(R.id.deselect_circle_item);

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

        gMapAdapter.setOnMapClickListener(latLng -> sheetBehavior.hide());
        gMapAdapter.setOnMarkerClickListener(marker ->
        {
            sheetBehavior.showHalf();
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
                List<GMarkerMetadata> selectedGCircleGMarkerMetadata = gMapAdapter.getSelectedGCircleGMarkerMetadata();
                if (selectedGCircleGMarkerMetadata == null)
                {
                    return false;
                }
                postsFragment = PostsFragment.newInstance(gMarkerMetadata,
                        selectedGCircleGMarkerMetadata);
            }
            else
            {
                postsFragment = PostsFragment.newInstance(gMarkerMetadata);
            }

            postsFragment.setOnDeselectItemClickListener(
                    view -> sheetBehavior.hide(gMapAdapter::deselectGMarker));
            postsFragment.setOnBackPressedCallback(new OnBackPressedCallback(true)
            {
                @Override
                public void handleOnBackPressed()
                {
                    sheetBehavior.hide(gMapAdapter::deselectGMarker);
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
            sheetBehavior.hide(
                    () -> mainViewModel.setSelectedAddressId(gCircleMeta.getAddressId()));
        });

        binding.toolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
        binding.toolbar.setNavigationOnClickListener(v -> binding.drawerLayout.open());
        binding.toolbar.inflateMenu(R.menu.menu_map);

        binding.toolbar.getMenu()
                       .findItem(R.id.deselect_circle_item)
                       .setOnMenuItemClickListener(item ->
                       {
                           sheetBehavior.hide(() -> mainViewModel.setSelectedAddressId(""));
                           return true;
                       });

        binding.selectedAddressesFab.setOnClickListener(v ->
        {
            if (mainViewModel.getSelectedAddressesCount() > 0)
            {
                PopupWindow popupWindow = new PopupWindow(fragment);
                popupWindow.setOnAddressClickListener(address ->
                {
                    v.setEnabled(false);
                    popupWindow.dismiss();
                    gMapAdapter.animateZoomTo(address, new GoogleMap.CancelableCallback()
                    {
                        @Override
                        public void onCancel()
                        {
                            v.setEnabled(true);
                        }

                        @Override
                        public void onFinish()
                        {
                            v.setEnabled(true);
                        }
                    });
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

        sheetBehavior.setAnimation(new PostsBottomSheetBehavior.Animation()
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

    private void initNavigationDrawer()
    {
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener()
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                onBackPressedCallback.setEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                onBackPressedCallback.setEnabled(false);
            }
        });
        binding.navigationView.setNavigationItemSelectedListener(this);

        NavHeaderMainBinding navHeaderMainBinding = NavHeaderMainBinding.bind(
                binding.navigationView.getHeaderView(0));

        mainViewModel.getUser()
                     .observe(fragment.getViewLifecycleOwner(),
                             user -> navHeaderMainBinding.fullNameView.setText(user.getFullName()));
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem item)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        String name;
        switch (item.getItemId())
        {
            case R.id.menu_profile:
                name = ProfileFragment.class.getSimpleName();
                transaction.replace(R.id.container, ProfileFragment.newInstance());
                break;
            case R.id.menu_addresses:
                name = UserAddressesFragment.class.getSimpleName();
                transaction.replace(R.id.container, UserAddressesFragment.newInstance());
                break;
            case R.id.menu_join_address:
                name = AddressesFragment.class.getSimpleName();
                transaction.replace(R.id.container, AddressesFragment.newInstance());
                break;
            default:
                name = "";
        }

        transaction.addToBackStack(name).commit();
        binding.drawerLayout.close();

        return false;
    }
}
