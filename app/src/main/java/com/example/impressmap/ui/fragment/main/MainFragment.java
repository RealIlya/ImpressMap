package com.example.impressmap.ui.fragment.main;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.adapter.gmap.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.main.mode.AddingMode;
import com.example.impressmap.ui.fragment.main.mode.CommonMode;
import com.example.impressmap.ui.fragment.main.mode.Mode;
import com.example.impressmap.util.SwitchableMode;
import com.google.android.gms.maps.SupportMapFragment;

public class MainFragment extends Fragment implements SwitchableMode
{
    public static final int COMMON_MODE = 0, ADDING_MODE = 1;

    private FragmentMainBinding binding;
    private GMapAdapter gMapAdapter;
    private MainFragmentViewModel viewModel;

    protected MainFragment()
    {
    }

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
                                 MainViewModel mainViewModel = new ViewModelProvider(
                                         requireActivity()).get(MainViewModel.class);
                                 FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                 if (fragmentManager.getBackStackEntryCount() > 0)
                                 {
                                     fragmentManager.popBackStack();
                                 }
                                 else if (mainViewModel.getMode().getValue() == ADDING_MODE)
                                 {
                                     mainViewModel.setMode(COMMON_MODE);
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
            supportMapFragment.getMapAsync(googleMap ->
            {
                gMapAdapter = new GMapAdapter(getContext(), googleMap, requireActivity());
                gMapAdapter.removeListeners();

                MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                        MainViewModel.class);
                mainViewModel.getMode().observe(getViewLifecycleOwner(), this::switchMode);
            });
        }
    }

    public void switchMode(int mode)
    {
        Mode modeClass;

        switch (mode)
        {
            case ADDING_MODE:
                modeClass = new AddingMode(this, viewModel, binding);
                break;
            default:
                modeClass = new CommonMode(this, viewModel, binding);
                break;
        }

        modeClass.switchOn(gMapAdapter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getViewModelStore().clear();
    }

    @Nullable
    @Override
    public Object getExitTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }
}
