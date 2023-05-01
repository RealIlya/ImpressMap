package com.example.impressmap.adapter.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.model.data.GObject;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.List;

public class GMapAdapterViewModel extends ViewModel
{
    private WeakReference<GMarker> lastSelectedGMarker;
    private WeakReference<GCircle> lastSelectedGCircle;

    public GMapAdapterViewModel()
    {
        lastSelectedGMarker = new WeakReference<>(null);
        lastSelectedGCircle = new WeakReference<>(null);
    }

    public void deselectLastGMarker()
    {
        if (lastSelectedGMarker.get() != null)
        {
            deselectObject(lastSelectedGMarker.get());
            lastSelectedGMarker = new WeakReference<>(null);
        }
    }

    public void deselectLastGCircle()
    {
        if (lastSelectedGCircle.get() != null)
        {
            deselectObject(lastSelectedGCircle.get());
            lastSelectedGCircle = new WeakReference<>(null);
        }
    }

    private void deselectObject(@NonNull GObject gObject)
    {
        gObject.setSelected(false);
    }

    public void setLastSelectedGMarker(GMarker lastSelectedGMarker)
    {
        this.lastSelectedGMarker = new WeakReference<>(lastSelectedGMarker);
    }

    public boolean isGCircleEqualsLastSelectedGCircle(@NonNull GCircle gCircle)
    {
        return gCircle.equals(lastSelectedGCircle.get());
    }

    public void setLastSelectedGCircle(GCircle lastSelectedGCircle)
    {
        this.lastSelectedGCircle = new WeakReference<>(lastSelectedGCircle);
    }

    public boolean inLastSelectedGCircle(LatLng latLng)
    {
        GCircle gCircle = lastSelectedGCircle.get();
        return gCircle != null && gCircle.inArea(latLng);
    }

    @Nullable
    public List<GMarker> getLastSelectedGCircleGMarkers()
    {
        GCircle gCircle = lastSelectedGCircle.get();
        return gCircle == null ? null : gCircle.getGCircleMeta().getGMarkers();
    }
}
