package com.example.impressmap.model;

import com.google.android.gms.maps.model.LatLng;

public class CircleMeta
{
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
}
