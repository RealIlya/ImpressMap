package com.example.impressmap.util;

import android.view.Window;

import androidx.core.view.WindowInsetsControllerCompat;

public abstract class WindowStatusBar
{
    public static void setLight(Window window,
                                boolean light)
    {
        new WindowInsetsControllerCompat(window,
                window.getDecorView()).setAppearanceLightStatusBars(light);
    }
}
