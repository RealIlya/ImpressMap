package com.example.impressmap.ui.fragment.bottommap.marker;

import static com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment.COMMON_MODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentCreatorCommonMarkerPreBinding;
import com.example.impressmap.util.SwitchableMode;

public class CreatorCommonMarkerPreFragment extends Fragment
{
    private FragmentCreatorCommonMarkerPreBinding binding;

    protected CreatorCommonMarkerPreFragment()
    {
    }

    @NonNull
    public static CreatorCommonMarkerPreFragment newInstance()
    {
        return new CreatorCommonMarkerPreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentCreatorCommonMarkerPreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        ((SwitchableMode) requireParentFragment()).switchMode(COMMON_MODE);

        binding.createCommonMarkerButton.setOnClickListener(v ->
        {
            String name = CreatorCommonMarkerInputFragment.class.getSimpleName();
            getParentFragmentManager().beginTransaction()
                                      .replace(R.id.info_container,
                                              CreatorCommonMarkerInputFragment.newInstance())
                                      .addToBackStack(name)
                                      .commit();
        });
    }
}
