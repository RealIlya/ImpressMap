package com.example.impressmap.model.data.gcircle;

import android.content.Context;

import androidx.annotation.ColorInt;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GObject;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;
import java.util.List;

public abstract class GCircle implements GObject
{
    private final Context context;
    private final Circle circle;
    private final GCircleMeta gCircleMeta;
    private final List<GMarker> gMarkers;
    private final double radius;
    private final float strokeWidth;
    private final int strokeColor;
    @ColorInt
    private final int selectedStateColor;
    @ColorInt
    private final int deselectedStateColor;
    private boolean selected;

    public GCircle(Context context,
                   Circle circle,
                   GCircleMeta gCircleMeta,
                   @ColorInt int selectedStateColor,
                   @ColorInt int deselectedStateColor,
                   @ColorInt int strokeColor)
    {
        this.context = context;
        this.circle = circle;
        this.gCircleMeta = gCircleMeta;
        this.selectedStateColor = selectedStateColor;
        this.deselectedStateColor = deselectedStateColor;
        this.strokeColor = strokeColor;

        radius = 20;
        strokeWidth = 3;

        circle.setRadius(radius);
        circle.setStrokeWidth(strokeWidth);
        circle.setFillColor(deselectedStateColor);
        circle.setStrokeColor(strokeColor);
        circle.setClickable(true);
        gMarkers = new ArrayList<>();
        selected = false;
    }

    public GCircleMeta getGCircleMeta()
    {
        return gCircleMeta;
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
