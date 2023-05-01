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
import com.example.impressmap.ui.viewmodel.MainFragmentViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.io.Closeable;
import java.util.List;

public class CommonMode extends Mode
{
    private final FragmentActivity activity;
    private final MainViewModel mainViewModel;
    private PostsAdapter postsAdapter;
    private NavigationDrawer navigationDrawer;
    private PostsBottomSheetBehavior<MaterialCardView> postsSheetBehavior;
    private GMapAdapter gMapAdapter;

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
        this.gMapAdapter = gMapAdapter;
        postsAdapter = new PostsAdapter();

        navigationDrawer = new NavigationDrawer(activity, binding.navigationView,
                binding.drawerLayout, activity.getSupportFragmentManager());

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.framePosts));

        Toolbar toolbar = binding.toolbar;
        Toolbar postsToolbar = binding.postsToolbar;

        LiveData<List<Address>> addressesLiveData = mainViewModel.getSelectedAddresses();

        if (!addressesLiveData.hasActiveObservers())
        {
            addressesLiveData.observe(fragment.getViewLifecycleOwner(), addressList ->
            {
                // not optimized
                gMapAdapter.clearMap();
                if (!addressList.isEmpty())
                {
                    for (Address address : addressList)
                    {
                        LiveData<List<GMarkerMetadata>> gMarkerLiveData = viewModel.getGMarkerMetadataByAddress(
                                address);
                        if (!gMarkerLiveData.hasActiveObservers())
                        {
                            //TODO only add. Must fix
                            gMarkerLiveData.observe(fragment.getViewLifecycleOwner(),
                                    gMapAdapter::addZone);
                            viewModel.addCloseable(new Closeable()
                            {
                                @Override
                                public void close()
                                {
                                    gMarkerLiveData.removeObservers(
                                            fragment.getViewLifecycleOwner());
                                }
                            });
                        }
                    }
                }
            });
        }

        LiveData<String> addressIdLiveData = mainViewModel.getSelectedAddressId();
        if (!addressIdLiveData.hasActiveObservers())
        {
            addressIdLiveData.observe(fragment.getViewLifecycleOwner(), addressId ->
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
        }

        // temporary
        gMapAdapter.zoomTo(new LatLng(54.849540, 83.106605));

        gMapAdapter.setOnMapLongClickListener(latLng ->
        {
            if (gMapAdapter.inSelectedGCircle(latLng))
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

            GMarkerMetadata gMarkerMetadata = ((GMarker) marker.getTag()).getGMarkerMetadata();
            MenuItem item = postsToolbar.getMenu().findItem(R.id.deselect_circle_item);
            if (gMarkerMetadata.getType() == GMarkerMetadata.COMMON_MARKER)
            {
                viewModel.getPostByGMarker(gMarkerMetadata)
                         .observe(fragment.getViewLifecycleOwner(), postsAdapter::addPost);

                item.setOnMenuItemClickListener(this::onDeselectMarker);
            }
            else if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                List<GMarker> gMarkers = gMapAdapter.getSelectedGCircleGMarkers();
                if (gMarkers != null)
                {
                    for (GMarker gMarker : gMarkers)
                    {
                        viewModel.getPostByGMarker(gMarker.getGMarkerMetadata())
                                 .observe(fragment.getViewLifecycleOwner(), postsAdapter::addPost);
                    }

                    item.setOnMenuItemClickListener(this::onDeselectCircle);
                }
            }

            return true;
        });
        gMapAdapter.setOnCircleClickListener(circle ->
        {
            GCircleMeta gCircleMeta = ((GCircle) circle.getTag()).getGCircleMeta();
            mainViewModel.setSelectedAddressId(gCircleMeta.getAddressId());
            postsSheetBehavior.hide();

            postsToolbar.getMenu()
                        .findItem(R.id.deselect_circle_item)
                        .setOnMenuItemClickListener(this::onDeselectCircle);
        });

        toolbar.setNavigationIcon(R.drawable.ic_menu_drawer);
        toolbar.setNavigationOnClickListener(v -> navigationDrawer.open());
        toolbar.inflateMenu(R.menu.menu_map);

        toolbar.getMenu()
               .findItem(R.id.deselect_circle_item)
               .setOnMenuItemClickListener(this::onDeselectCircle);
        toolbar.getMenu().findItem(R.id.deselect_circle_item).setVisible(false);

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
                View actionView = toolbar.getMenu()
                                         .findItem(R.id.deselect_circle_item)
                                         .getActionView();
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


    private boolean onDeselectMarker(MenuItem menuItem)
    {
        gMapAdapter.deselectGMarker();
        postsSheetBehavior.hide();
        return true;
    }

    private boolean onDeselectCircle(MenuItem menuItem)
    {
        mainViewModel.setSelectedAddressId("");
        postsSheetBehavior.hide();
        return true;
    }
}
