package com.example.impressmap.ui.fragment.bottommap.mapinfo;

import static com.example.impressmap.util.Constants.LAT_LNG_KEY;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentMapInfoBinding;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.mode.CommonMode;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.mode.Mode;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.mode.NavigatingMode;
import com.example.impressmap.ui.fragment.bottommap.marker.CreatorCommonMarkerPreFragment;
import com.example.impressmap.util.Locations;
import com.example.impressmap.util.SwitchableMode;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MapInfoFragment extends BottomSheetDialogFragment implements SwitchableMode, MenuHost
{
    public static final int COMMON_MODE = 0;
    public static final int NAVIGATING_MODE = 1;
    private static final String IN_ZONE_KEY = "IN_ZONE_KEY";
    private FragmentMapInfoBinding binding;
    private MapInfoFragmentViewModel viewModel;

    private DialogInterface.OnDismissListener onDismissListener;

    protected MapInfoFragment()
    {
    }

    @NonNull
    public static MapInfoFragment newInstance(@NonNull LatLng latLng,
                                              boolean inZone)
    {
        Bundle arguments = new Bundle();
        arguments.putDoubleArray(LAT_LNG_KEY, new double[]{latLng.latitude, latLng.longitude});
        arguments.putBoolean(IN_ZONE_KEY, inZone);

        MapInfoFragment mapInfoFragment = new MapInfoFragment();
        mapInfoFragment.setArguments(arguments);
        return mapInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentMapInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(MapInfoFragmentViewModel.class);

        double[] rawLatLng = requireArguments().getDoubleArray(LAT_LNG_KEY);
        LatLng latLng = new LatLng(rawLatLng[0], rawLatLng[1]);
        boolean inZone = requireArguments().getBoolean(IN_ZONE_KEY);
        viewModel.setLatLng(latLng);

        switchMode(COMMON_MODE);

        Location location = Locations.getOneFromLatLng(getContext(), latLng);

        binding.toolbar.setTitle(location != null ? location.getFullAddressReversed()
                                                  : getString(R.string.not_stated));
        binding.toolbar.setSubtitle(latLng.latitude + " : " + latLng.longitude);

        if (inZone)
        {
            getChildFragmentManager().beginTransaction()
                                     .replace(R.id.info_container,
                                             CreatorCommonMarkerPreFragment.newInstance())
                                     .commit();
        }

        setCancelable(false);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        onDismissListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        onDismissListener.onDismiss(dialog);
    }

    @Override
    public void switchMode(int mode)
    {
        Mode modeClass;

        switch (mode)
        {
            case COMMON_MODE:
                modeClass = new CommonMode(this);
                break;
            default:
                modeClass = new NavigatingMode(this);
                break;
        }

        modeClass.switchOn(binding.toolbar);
    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider)
    {
        binding.toolbar.addMenuProvider(provider);
    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider,
                                @NonNull LifecycleOwner owner)
    {
        binding.toolbar.addMenuProvider(provider, owner);
    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider,
                                @NonNull LifecycleOwner owner,
                                @NonNull Lifecycle.State state)
    {
        binding.toolbar.addMenuProvider(provider, owner, state);
    }

    @Override
    public void removeMenuProvider(@NonNull MenuProvider provider)
    {
        binding.toolbar.removeMenuProvider(provider);
    }

    @Override
    public void invalidateMenu()
    {
        binding.toolbar.invalidateMenu();
    }
}
