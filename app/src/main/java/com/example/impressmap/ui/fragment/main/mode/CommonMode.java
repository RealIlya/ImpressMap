package com.example.impressmap.ui.fragment.main.mode;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
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
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CommonMode extends Mode
{
    private final FragmentActivity activity;
    private PostsAdapter postsAdapter;
    private NavigationDrawer navigationDrawer;
    private PostsBottomSheetBehavior<MaterialCardView> postsSheetBehavior;
    private MainViewModel mainViewModel;

    public CommonMode(MainFragment fragment,
                      FragmentMainBinding binding)
    {
        super(fragment, binding);
        activity = fragment.requireActivity();
    }

    @Override
    public void switchOn(GMapAdapter gMapAdapter)
    {
        postsAdapter = new PostsAdapter();

        navigationDrawer = new NavigationDrawer(activity, binding.navigationView,
                binding.drawerLayout, activity.getSupportFragmentManager());

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.framePosts));

        mainViewModel = new ViewModelProvider(activity).get(MainViewModel.class);

        Toolbar toolbar = binding.toolbar;
        MenuItem toolbarDeselectionItem = toolbar.getMenu().findItem(R.id.deselect_circle_menu);

        Toolbar postsToolbar = binding.postsToolbar;
        MenuItem postsToolbarDeselectionItem = postsToolbar.getMenu()
                                                           .findItem(R.id.deselect_circle_menu);

        LiveData<List<Address>> addressesLiveData = mainViewModel.getSelectedAddresses();
        if (!addressesLiveData.hasObservers())
        {
            addressesLiveData.observe(fragment, addressList ->
            {
                // not optimized
                gMapAdapter.clearMap();
                if (!addressList.isEmpty())
                {
                    for (Address address : addressList)
                    {
                        viewModel.getGMarkerMetadataByAddress(address)
                                 .observe(fragment.getViewLifecycleOwner(), gMapAdapter::addZone);
                    }
                }
            });
        }

        LiveData<String> addressIdLiveData = mainViewModel.getSelectedAddressId();
        if (!addressIdLiveData.hasObservers())
        {
            addressIdLiveData.observe(fragment.getViewLifecycleOwner(), addressId ->
            {
                if (addressId.isEmpty())
                {
                    if (toolbarDeselectionItem != null)
                    {
                        toolbarDeselectionItem.setVisible(false);
                    }
                    gMapAdapter.deselectLastCircle();
                }
                else
                {
                    if (toolbarDeselectionItem != null)
                    {
                        toolbarDeselectionItem.setVisible(true);
                    }
                }
            });
        }

        // temporary
        gMapAdapter.zoomTo(new LatLng(54.849540, 83.106605));

        gMapAdapter.setOnMapLongClickListener(latLng ->
        {
            if (!addressIdLiveData.getValue().isEmpty())
            {
                gMapAdapter.setPointer(latLng);
                gMapAdapter.zoomTo(latLng);

                MapInfoFragment mapInfoFragment = MapInfoFragment.newInstance(latLng);
                String name = MapInfoFragment.class.getSimpleName();
                mapInfoFragment.show(activity.getSupportFragmentManager(), name);
                mapInfoFragment.setOnDismissListener(
                        dialogInterface -> gMapAdapter.removePointer());
            }
        });
        gMapAdapter.setOnMapClickListener(latLng -> postsSheetBehavior.hide());
        gMapAdapter.setOnMarkerClickListener(marker ->
        {
            postsAdapter.clear();

            postsSheetBehavior.showHalf();
            postsToolbar.setTitle(marker.getTitle());

            mainViewModel.setLastSelectedMarker(marker);

            GMarkerMetadata gMarkerMetadata = ((GMarker) marker.getTag()).getGMarkerMetadata();
            if (gMarkerMetadata.getType() == GMarkerMetadata.COMMON_MARKER)
            {
                viewModel.getPostByGMarker(gMarkerMetadata)
                         .observe(fragment.getViewLifecycleOwner(), postsAdapter::addPost);

                postsToolbarDeselectionItem.setOnMenuItemClickListener(menuItem ->
                {
                    gMapAdapter.deselectLastMarker();
                    postsSheetBehavior.hide();
                    return true;
                });
            }
            else if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                List<GMarker> gMarkers = gMapAdapter.getLastSelectedCircleGMarkers();
                if (gMarkers != null)
                {
                    for (GMarker gMarker : gMarkers)
                    {
                        viewModel.getPostByGMarker(gMarker.getGMarkerMetadata())
                                 .observe(fragment.getViewLifecycleOwner(), postsAdapter::addPost);
                    }

                    postsToolbarDeselectionItem.setOnMenuItemClickListener(this::onDeselectCircle);
                }
            }

            return true;
        });
        gMapAdapter.setOnCircleClickListener(circle ->
        {
            GCircleMeta gCircleMeta = ((GCircle) circle.getTag()).getGCircleMeta();
            mainViewModel.setSelectedAddressId(gCircleMeta.getAddressId());
            postsSheetBehavior.hide();

            mainViewModel.setLastSelectedCircle(circle);

            postsToolbarDeselectionItem.setOnMenuItemClickListener(this::onDeselectCircle);
        });

        toolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
        toolbar.setNavigationOnClickListener(v -> navigationDrawer.open());
        toolbar.inflateMenu(R.menu.menu_map);

        toolbarDeselectionItem.setOnMenuItemClickListener(this::onDeselectCircle);
        toolbarDeselectionItem.setVisible(false);

        postsSheetBehavior.setAnimation(new PostsBottomSheetBehavior.Animation()
        {
            @Override
            public void onStateChanged(int newState)
            {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    binding.framePosts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(float slideOffset)
            {
                View actionView = toolbarDeselectionItem.getActionView();
                if (actionView != null)
                {
                    actionView.animate().alpha(slideOffset).start();
                }
            }
        });

        RecyclerView recyclerView = binding.postsRecyclerView;
        recyclerView.setAdapter(postsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private boolean onDeselectCircle(MenuItem menuItem)
    {
        mainViewModel.setSelectedAddressId("");
        postsSheetBehavior.hide();
        return true;
    }
}
