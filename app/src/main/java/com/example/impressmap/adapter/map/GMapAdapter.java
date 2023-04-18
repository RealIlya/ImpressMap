package com.example.impressmap.adapter.map;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.markers.AddressGMarker;
import com.example.impressmap.model.data.markers.CommonGMarker;
import com.example.impressmap.model.data.markers.GMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class GMapAdapter extends MapAdapter
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
                onMapLongClicked(latLng);
            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                /*if (googleMap.getCameraPosition().zoom < MIN_ZOOM)
                {
                    hideMarkers();
                }
                else
                {
                    showMarkers();
                }*/
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                Object markerTag = marker.getTag();
                if (markerTag == null)
                {
                    return false;
                }

                deselectLastMarker();

                lastSelected = marker;

                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));

                GMarker gMarker = (GMarker) markerTag;
                gMarker.setSelected(true);

                return onMarkerClicked(marker);
            }
        });
    }

    public void setItems(List<GMarkerMetadata> gMarkerMetadataList)
    {
        clearMap();
        for (GMarkerMetadata gMarkerMetadata : gMarkerMetadataList)
        {
            addMarker(gMarkerMetadata);
        }
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