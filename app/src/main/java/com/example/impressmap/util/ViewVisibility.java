package com.example.impressmap.util;

import android.view.View;

public class ViewVisibility
{
    /**
     * <p>If visible is true returns VISIBLE</p>
     * <p>If visible is false returns GONE</p>
     */
    public static int show(boolean visible)
    {
        return visible ? View.VISIBLE : View.GONE;
    }
}
