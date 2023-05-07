package com.example.impressmap.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class PositionPreferences
{
    private static final String POSITION_PREFS = "POSITION_PREFS";
    private static final String LAT_KEY = "LAT_KEY";
    private static final String LNG_KEY = "LNG_KEY";
    private static final String ZOOM_KEY = "ZOOM_KEY";
    private final SharedPreferences preferences;

    public PositionPreferences(@NonNull Context context)
    {
        preferences = context.getSharedPreferences(POSITION_PREFS, Context.MODE_PRIVATE);
    }

    public void removePosition()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(LAT_KEY);
        editor.remove(LNG_KEY);
        editor.remove(ZOOM_KEY);
        editor.apply();
    }

    @Nullable
    public CameraPosition getPosition()
    {
        boolean positionExists = preferences.contains(LAT_KEY) && preferences.contains(
                LNG_KEY) && preferences.contains(ZOOM_KEY);

        if (positionExists)
        {
            float latitude = preferences.getFloat(LAT_KEY, 0);
            float longitude = preferences.getFloat(LNG_KEY, 0);
            float zoom = preferences.getFloat(ZOOM_KEY, 0);

            LatLng latLng = new LatLng(latitude, longitude);
            return CameraPosition.fromLatLngZoom(latLng, zoom);
        }
        return null;
    }

    public void setPosition(@NonNull CameraPosition cameraPosition)
    {
        SharedPreferences.Editor editor = preferences.edit();

        LatLng latLng = cameraPosition.target;
        float latitude = (float) latLng.latitude;
        float longitude = (float) latLng.longitude;
        float zoom = cameraPosition.zoom;

        editor.putFloat(LAT_KEY, latitude);
        editor.putFloat(LNG_KEY, longitude);
        editor.putFloat(ZOOM_KEY, zoom);
        editor.apply();
    }
}
