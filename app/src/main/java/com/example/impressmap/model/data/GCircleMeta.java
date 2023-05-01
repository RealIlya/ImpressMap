package com.example.impressmap.model.data;

import androidx.annotation.Nullable;

import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GCircleMeta
{
    private final List<GMarker> gMarkers = new ArrayList<>();
    private String addressId = "";
    private LatLng center = new LatLng(0, 0);

    public String getAddressId()
    {
        return addressId;
    }

    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }

    public LatLng getCenter()
    {
        return center;
    }

    public void setCenter(LatLng center)
    {
        this.center = center;
    }

    public List<GMarker> getGMarkers()
    {
        return gMarkers;
    }

    public void setGMarkers(List<GMarker> gMarkers)
    {
        this.gMarkers.addAll(gMarkers);
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
        if (!(obj instanceof GCircleMeta))
        {
            return false;
        }
        GCircleMeta o = (GCircleMeta) obj;
        return o.addressId.equals(addressId);
    }
}
