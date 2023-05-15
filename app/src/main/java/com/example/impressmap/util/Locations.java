package com.example.impressmap.util;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.impressmap.model.data.Address;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
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
    private static Address locationToAddress(@NonNull android.location.Address location)
    {
        Address address = new Address();
        String country = location.getCountryName();
        String city = location.getLocality();
        String state = location.getAdminArea();
        String street = location.getThoroughfare();
        String house = location.getSubThoroughfare();
        if (country != null && city != null && state != null && street != null && house != null)
        {
            address.setFullAddress(
                    String.format("%s|%s|%s|%s|%s", country, city, state, street, house));
            return address;
        }

        return null;
    }

    @Nullable
    public static Address getOneFromLatLng(Context context,
                                           @NonNull LatLng latLng)
    {
        try
        {
            List<android.location.Address> locations = new Geocoder(context,
                    Locale.ENGLISH).getFromLocation(latLng.latitude, latLng.longitude, 6);

            for (android.location.Address location : locations)
            {
                Address address = locationToAddress(location);
                if (address != null)
                {
                    return address;
                }
            }

            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    @Nullable
    public static List<Address> getFromLatLng(Context context,
                                              @NonNull LatLng latLng)
    {
        try
        {
            List<android.location.Address> locations = new Geocoder(context,
                    Locale.ENGLISH).getFromLocation(latLng.latitude, latLng.longitude, 6);

            List<Address> addresses = new ArrayList<>();
            for (android.location.Address location : locations)
            {
                Address address = locationToAddress(location);
                if (address != null)
                {
                    addresses.add(address);
                }
            }

            return addresses;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}
