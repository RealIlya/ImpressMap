package com.example.impressmap.model.data.gcircle;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GObject;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public abstract class GCircle implements GObject
{
    private final Context context;
    private final Circle circle;
    private final GCircleMeta gCircleMeta;
    private final float strokeWidth;
    private final int strokeColor;
    @ColorInt
    private final int selectedStateColor;
    @ColorInt
    private final int deselectedStateColor;
    private double radius;
    private boolean selected;

    public GCircle(Context context,
                   @NonNull Circle circle,
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
        selected = false;
    }

    public GCircleMeta getGCircleMeta()
    {
        return gCircleMeta;
    }

    /*private void defineRadius()
    {
        float max = 20;

        for (GMarker gMarker : gCircleMeta.getGMarkers())
        {
            LatLng latLng = gMarker.getGMarkerMetadata().getPosition();
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;

            LatLng center = gCircleMeta.getCenter();
            float distance = Locations.getDistance(latitude, longitude, center.latitude,
                    center.longitude)[0];
            if (distance > max)
            {
                max = distance;
            }
        }

        radius = max;
    }*/

    public void enableGMarkersClickable()
    {
        setGMarkersClickable(true);
    }

    public void disableGMarkersClickable()
    {
        setGMarkersClickable(false);
    }

    private void setGMarkersClickable(boolean clickable)
    {
        for (GMarker gMarker : gCircleMeta.getGMarkers())
        {
            if (!clickable)
            {
                gMarker.setSelected(false);
            }
            gMarker.setClickable(clickable);
        }
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

        if (selected)
        {
            circle.setFillColor(selectedStateColor);
            enableGMarkersClickable();
        }
        else
        {
            circle.setFillColor(deselectedStateColor);
            disableGMarkersClickable();
        }

        this.selected = selected;
    }

    public boolean inArea(@NonNull LatLng latLng)
    {
        double startLatitude = latLng.latitude;
        double startLongitude = latLng.longitude;
        LatLng center = gCircleMeta.getCenter();
        double endLatitude = center.latitude;
        double endLongitude = center.longitude;
        float distance = Locations.getDistance(startLatitude, startLongitude, endLatitude,
                endLongitude);
        return distance <= radius;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof GCircle))
        {
            return false;
        }
        GCircle o = (GCircle) obj;
        return o.gCircleMeta.equals(gCircleMeta);
    }
}
