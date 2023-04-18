package com.example.impressmap.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class Locations
{
    @Nullable
    public static Address getFromLocation(Context context,
                                          LatLng latLng)
    {
        try
        {
            List<Address> location = new Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, 1);
            return location.size() > 0 ? location.get(0) : null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    /**
     * @return The address line. 0 - country, 1 - city, 2 - state
     */
    @Nullable
    public static String[] getAddressLine(Context context,
                                          LatLng latLng)
    {
        Address location = getFromLocation(context, latLng);

        if (location != null)
        {
            return new String[]{location.getCountryName(), location.getLocality(), location.getAdminArea()};
        }
        else
        {
            return null;
        }
    }
}