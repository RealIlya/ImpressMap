package com.example.impressmap.model.data.gmarker;

import android.content.Context;

import androidx.annotation.DrawableRes;

import com.example.impressmap.util.Converter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

public abstract class GMarker
{
    private final Context context;
    private final Marker marker;
    @DrawableRes
    private final int selectedStateResId;
    @DrawableRes
    private final int deselectedStateResId;
    private boolean selected;

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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        if (this.selected == selected)
        {
            return;
        }

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Converter.drawableIdToBitmap(context,
                selected ? selectedStateResId : deselectedStateResId)));

        this.selected = selected;
    }
}
