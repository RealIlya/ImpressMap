package com.example.impressmap.model.data.gcircle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.util.Locations;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public abstract class GCircle
{
    public final float STROKE_WIDTH = 3;
    public final double RADIUS = 20;
    private final Circle circle;
    private final GCircleMeta gCircleMeta;
    @ColorInt
    private final int selectedStateColor;
    @ColorInt
    private final int deselectedStateColor;
    private boolean selected;

    public GCircle(@NonNull Circle circle,
                   GCircleMeta gCircleMeta,
                   @ColorInt int selectedStateColor,
                   @ColorInt int deselectedStateColor,
                   @ColorInt int strokeColor)
    {
        this.circle = circle;
        this.gCircleMeta = gCircleMeta;
        this.selectedStateColor = selectedStateColor;
        this.deselectedStateColor = deselectedStateColor;

        circle.setRadius(RADIUS);
        circle.setStrokeWidth(STROKE_WIDTH);
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

    private void setGMarkerSelected(boolean selected)
    {
        for (GMarker gMarker : gCircleMeta.getGMarkers())
        {
            gMarker.setSelected(selected);
        }
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
        }
        else
        {
            circle.setFillColor(deselectedStateColor);
            setGMarkerSelected(false);
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
        return distance <= RADIUS;
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
