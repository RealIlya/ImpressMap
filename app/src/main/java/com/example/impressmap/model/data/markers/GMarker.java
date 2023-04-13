package com.example.impressmap.model.data.markers;

import android.content.Context;

import androidx.annotation.DrawableRes;

import com.example.impressmap.util.Converter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

public abstract class GMarker
{
    private final Context context;
    private Marker marker;
    private boolean selected;
    @DrawableRes
    private int selectedStateResId;

    @DrawableRes
    private int deselectedStateResId;

    public GMarker(Context context,
                   Marker marker,
                   @DrawableRes int selectedStateResId,
                   @DrawableRes int deselectedStateResId)
    {
        this.context = context;
        this.marker = marker;
        this.selectedStateResId = selectedStateResId;
        this.deselectedStateResId = deselectedStateResId;

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                Converter.drawableIdToBitmap(context, deselectedStateResId)));
        selected = false;
    }

    public void setSelected(boolean selected)
    {
        if (this.selected == selected)
        {
            return;
        }

        if (selected)
        {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    Converter.drawableIdToBitmap(context, selectedStateResId)));
        }
        else
        {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    Converter.drawableIdToBitmap(context, deselectedStateResId)));
        }

        this.selected = selected;
    }

    public boolean isSelected()
    {
        return selected;
    }
}
