package com.example.impressmap.adapter.gmap;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.GObject;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.example.impressmap.preference.PositionPreferences;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GMapAdapterViewModel extends AndroidViewModel
{
    private final PositionPreferences positionPreferences;
    private final List<GMarker> gMarkersCache;
    private final List<GCircle> gCirclesCache;
    private WeakReference<GMarker> lastSelectedGMarker;
    private WeakReference<GCircle> lastSelectedGCircle;

    public GMapAdapterViewModel(Application application)
    {
        super(application);
        positionPreferences = new PositionPreferences(application);
        lastSelectedGMarker = new WeakReference<>(null);
        lastSelectedGCircle = new WeakReference<>(null);
        gMarkersCache = new ArrayList<>();
        gCirclesCache = new ArrayList<>();
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

    public void addGMarkerInCache(GMarker gMarker)
    {
        gMarkersCache.add(gMarker);
    }

    public void addGCircleInCache(GCircle gCircle)
    {
        gCirclesCache.add(gCircle);
    }

    public boolean inGMarkersCache(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        for (GMarker gMarker : gMarkersCache)
        {
            if (gMarkerMetadata.equals(gMarker.getGMarkerMetadata()))
            {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public GCircle findGCircleByGMarkerMetadata(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        for (GCircle gCircle : gCirclesCache)
        {
            if (gCircle.getGCircleMeta().getAddressId().equals(gMarkerMetadata.getId()))
            {
                return gCircle;
            }
        }
        return null;
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

    public CameraPosition getLastCameraPosition()
    {
        return positionPreferences.getPosition();
    }

    public void setLastCameraPosition(CameraPosition lastCameraPosition)
    {
        positionPreferences.setPosition(lastCameraPosition);
    }

    @Nullable
    public List<GMarker> getLastSelectedGCircleGMarkers()
    {
        GCircle gCircle = lastSelectedGCircle.get();
        return gCircle == null ? null : gCircle.getGCircleMeta().getGMarkers();
    }

    @Nullable
    public GCircle getGCircle(Address address)
    {
        for (GCircle gCircle : gCirclesCache)
        {
            if (gCircle.getGCircleMeta().getAddressId().equals(address.getId()))
            {
                return gCircle;
            }
        }

        return null;
    }

    public void clearCache()
    {
        gMarkersCache.clear();
        gCirclesCache.clear();
    }
}
