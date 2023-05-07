package com.example.impressmap.ui.fragment.bottom.mode;

import androidx.annotation.NonNull;

import com.example.impressmap.R;
import com.example.impressmap.ui.fragment.bottom.MapInfoFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class NavigatingMode extends Mode
{
    public NavigatingMode(@NonNull MapInfoFragment fragment)
    {
        super(fragment);
    }

    @Override
    public void switchOn(@NonNull MaterialToolbar toolbar)
    {
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener(
                v -> fragment.getChildFragmentManager().popBackStack());
    }
}
