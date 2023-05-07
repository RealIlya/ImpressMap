package com.example.impressmap.ui.fragment.bottom.mode;

import androidx.annotation.NonNull;

import com.example.impressmap.ui.fragment.bottom.MapInfoFragment;
import com.google.android.material.appbar.MaterialToolbar;

public abstract class Mode
{
    protected MapInfoFragment fragment;

    public Mode(@NonNull MapInfoFragment fragment)
    {
        this.fragment = fragment;
    }

    public abstract void switchOn(@NonNull MaterialToolbar toolbar);
}
