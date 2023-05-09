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
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.adapter.post.PostsAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.ui.NavigationDrawer;
import com.example.impressmap.ui.fragment.CommentsFragment;
import com.example.impressmap.ui.fragment.bottommap.MapInfoFragment;
import com.example.impressmap.ui.fragment.bottomposts.PostsBottomSheetBehavior;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.MainFragmentViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

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
        postsAdapter = new PostsAdapter(activity);

        navigationDrawer = new NavigationDrawer(activity, binding.navigationView,
                binding.drawerLayout, activity.getSupportFragmentManager());

        postsSheetBehavior = new PostsBottomSheetBehavior<>(
                BottomSheetBehavior.from(binding.framePosts), activity);

        Toolbar toolbar = binding.toolbar;
        Toolbar postsToolbar = binding.postsToolbar;

        LiveData<List<Address>> addressesLiveData = mainViewModel.getSelectedAddresses();
        if (!addressesLiveData.hasActiveObservers())
        {
            addressesLiveData.observe(activity, addressList ->
            {
                // not optimized
                gMapAdapter.clearMap();
                if (!addressList.isEmpty())
                {
                    for (Address address : addressList)
                    {
                        //TODO only add. Must fix
                        viewModel.getGMarkerMetadataByAddress(address)
                                 .observeForever(gMapAdapter::addZone);
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
            postsAdapter.clear();

            postsSheetBehavior.showHalf();
            GMarkerMetadata gMarkerMetadata = ((GMarker) marker.getTag()).getGMarkerMetadata();

            postsToolbar.setTitle(gMarkerMetadata.getTitle());

            MenuItem item = postsToolbar.getMenu().findItem(R.id.deselect_circle_item);
            if (gMarkerMetadata.getType() == GMarkerMetadata.COMMON_MARKER)
            {
                viewModel.getPostByGMarker(gMarkerMetadata).observeForever(postsAdapter::addPost);

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
                                 .observeForever(postsAdapter::addPost);
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

        postsAdapter.setOnCommentsButtonClickListener((view, post) ->
        {
            String name = CommentsFragment.class.getSimpleName();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CommentsFragment.newInstance(post))
                    .addToBackStack(name)
                    .commit();
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
