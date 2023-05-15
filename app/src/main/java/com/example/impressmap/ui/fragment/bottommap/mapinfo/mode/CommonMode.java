package com.example.impressmap.ui.fragment.bottommap.mapinfo.mode;

import androidx.annotation.NonNull;

import com.example.impressmap.R;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class CommonMode extends Mode
{
    public CommonMode(@NonNull MapInfoFragment fragment)
    {
        super(fragment);
    }

    @Override
    public void switchOn(@NonNull MaterialToolbar toolbar)
    {
        toolbar.setNavigationIcon(R.drawable.ic_hide);
        toolbar.setNavigationOnClickListener(v -> fragment.dismiss());
    }
}
