package com.example.impressmap.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Locations
{
    public static float getDistance(double startLatitude,
                                    double startLongitude,
                                    double endLatitude,
                                    double endLongitude)
    {
        float[] distance = new float[2];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude,
                distance);

        return distance[0];
    }

    @Nullable
    public static Address getFromLatLng(Context context,
                                        @NonNull LatLng latLng)
    {
        try
        {
            List<Address> locations = new Geocoder(context, Locale.ENGLISH).getFromLocation(
                    latLng.latitude, latLng.longitude, 3);

            for (Address location : locations)
            {
                String house1 = location.getFeatureName();
                String house2 = location.getSubThoroughfare();
                String subLocality = location.getSubLocality();
                String thoroughfare = location.getThoroughfare();
                if ((house1 != null || house2 != null) && (subLocality != null || thoroughfare != null))
                {
                    return location;
                }
            }

            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
