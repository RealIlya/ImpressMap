package com.example.impressmap.ui.fragment.main.mode;

import static com.example.impressmap.ui.fragment.main.MainFragment.COMMON_MODE;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.adapter.map.GMapAdapter;
import com.example.impressmap.databinding.FragmentMainBinding;
import com.example.impressmap.ui.fragment.CreatorAddressFragment;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.MainFragmentViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;

public class AddingMode extends Mode
{
    private final FragmentActivity activity;
    private MainViewModel mainViewModel;

    public AddingMode(MainFragment fragment,
                      MainFragmentViewModel viewModel,
                      FragmentMainBinding binding)
    {
        super(fragment, viewModel, binding);
        activity = fragment.requireActivity();
    }

    @Override
    public void switchOn(GMapAdapter gMapAdapter)
    {
        mainViewModel = new ViewModelProvider(activity).get(MainViewModel.class);

        gMapAdapter.setOnMapClickListener(latLng ->
        {
//            gMapAdapter.setPointer(latLng);
            gMapAdapter.zoomTo(latLng);

            CreatorAddressFragment fragment = CreatorAddressFragment.newInstance(latLng);
            String name = CreatorAddressFragment.class.getSimpleName();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(name)
                    .commit();
        });

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow);
        binding.toolbar.setNavigationOnClickListener(v -> mainViewModel.setMode(COMMON_MODE));
        binding.toolbar.getMenu().clear();

        binding.framePosts.setVisibility(View.INVISIBLE);
    }
}
