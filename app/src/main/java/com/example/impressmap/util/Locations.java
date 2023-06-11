package com.example.impressmap.util;

import android.content.Context;
import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.example.impressmap.model.data.Location;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Locations
{
    /**
     * <p>Returns distance between two points</p>
     */
    public static float getDistance(double startLatitude,
                                    double startLongitude,
                                    double endLatitude,
                                    double endLongitude)
    {
        float[] distance = new float[2];
        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude,
                endLongitude, distance);

        return distance[0];
    }

    /**
     * <p>Converts gms' location to Application's package location</p>
     */
    @Nullable
    private static Location convertLocation(@NonNull android.location.Address location)
    {
        Location locationResult = new Location();
        String country = location.getCountryName();
        String city = location.getLocality();
        String state = location.getAdminArea();
        String street = location.getThoroughfare();
        String house = location.getSubThoroughfare();
        if (country != null && city != null && state != null && street != null && house != null)
        {
            locationResult.setFullAddress(
                    String.format("%s|%s|%s|%s|%s", country, city, state, street, house));
            return locationResult;
        }

        return null;
    }

    /**
     * <p>Returns a fit location by latLng</p>
     */
    @Nullable
    public static Location getOneFromLatLng(Context context,
                                            @NonNull LatLng latLng)
    {
        try
        {
            List<android.location.Address> locations = new Geocoder(context,
                    Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 6);

            for (android.location.Address address : locations)
            {
                Location location = convertLocation(address);
                if (location != null)
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

    /**
     * <p>Returns a locations list by latLng</p>
     */
    @Nullable
    public static List<Location> getFromLatLng(Context context,
                                               @NonNull LatLng latLng)
    {
        try
        {
            List<android.location.Address> addresses = new Geocoder(context,
                    Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 6);

            List<Location> locations = new ArrayList<>();
            for (android.location.Address address : addresses)
            {
                Location location = convertLocation(address);
                if (location != null)
                {
                    locations.add(location);
                }
            }

            return locations;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
