package com.example.impressmap.model.data.gcircle;

import android.content.Context;

import androidx.annotation.ColorInt;

import com.google.android.gms.maps.model.Circle;

public abstract class GCircle
{
    private final Context context;
    private final Circle circle;
    private final double radius;
    private final float strokeWidth;
    private final int strokeColor;
    @ColorInt
    private final int selectedStateColor;
    @ColorInt
    private final int deselectedStateColor;
    private String addressId = "";
    private boolean selected;

    public GCircle(Context context,
                   Circle circle,
                   @ColorInt int selectedStateColor,
                   @ColorInt int deselectedStateColor,
                   @ColorInt int strokeColor)
    {
        this.context = context;
        this.circle = circle;
        this.selectedStateColor = selectedStateColor;
        this.deselectedStateColor = deselectedStateColor;
        this.strokeColor = strokeColor;

        radius = 10;
        strokeWidth = 3;

        circle.setRadius(radius);
        circle.setStrokeWidth(strokeWidth);
        circle.setFillColor(deselectedStateColor);
        circle.setStrokeColor(strokeColor);
        circle.setClickable(true);
        selected = false;
    }

    public String getAddressId()
    {
        return addressId;
    }

    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
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

        circle.setFillColor(selected ? selectedStateColor : deselectedStateColor);

        this.selected = selected;
    }
}
