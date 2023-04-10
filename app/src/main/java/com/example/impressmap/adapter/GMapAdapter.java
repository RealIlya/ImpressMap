package com.example.impressmap.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.impressmap.model.data.markers.AddressGMarker;
import com.example.impressmap.model.data.markers.CommonGMarker;
import com.example.impressmap.model.data.markers.GMarker;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class GMapAdapter extends Adapter
{
    private final Context context;

    public GMapAdapter(Context context,
                       GoogleMap googleMap)
    {
        super(googleMap);
        this.context = context;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {
                deselectLastMarker();

                onMapClicked(latLng);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng)
            {

            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                if (googleMap.getCameraPosition().zoom < MIN_ZOOM)
                {
                    hideMarkers();
                }
                else
                {
                    showMarkers();
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                deselectLastMarker();

                lastSelected = marker;

                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));

                GMarker gMarker = (GMarker) marker.getTag();
                gMarker.setSelected(true);

                return onMarkerClicked(marker);
            }
        });
    }

    public Marker addMarker(GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = super.addMarker(gMarkerMetadata);

        GMarker gMarker = null;
        switch (gMarkerMetadata.getType())
        {
            case GMarkerMetadata.ADDRESS_MARKER:
                gMarker = new AddressGMarker(context, marker);
                break;
            case GMarkerMetadata.COMMON_MARKER:
                gMarker = new CommonGMarker(context, marker);
                break;
        }

        marker.setTag(gMarker);

        return marker;
    }
}
