package com.example.impressmap.adapter.map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.CommonGCircle;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.AddressGMarker;
import com.example.impressmap.model.data.gmarker.CommonGMarker;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
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

                GMarker gMarker = (GMarker) markerTag;

                if (gMarker.isClickable())
                {
                    deselectLastMarker();

                    lastSelectedMarker = marker;

                    googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));

                    gMarker.setSelected(true);

                    return onMarkerClicked(marker);
                }

                return true;
            }
        });

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener()
        {
            @Override
            public void onCircleClick(@NonNull Circle circle)
            {
                Object circleTag = circle.getTag();
                if (circleTag == null)
                {
                    return;
                }

                GCircle gCircle = (GCircle) circleTag;
                deselectLastCircle();

                lastSelectedCircle = circle;

                gCircle.setSelected(true);

                onCircleClicked(circle);
            }
        });
    }

    public void addZone(List<GMarkerMetadata> gMarkerMetadataList)
    {
        List<GMarker> gMarkers = new ArrayList<>();
        GCircleMeta gCircleMeta = new GCircleMeta();
        for (GMarkerMetadata gMarkerMetadata : gMarkerMetadataList)
        {
            gMarkers.add((GMarker) addMarker(gMarkerMetadata).getTag());
            if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                gCircleMeta.setAddressId(gMarkerMetadata.getId());
                gCircleMeta.setCenter(gMarkerMetadata.getPosition());
            }
        }

        gCircleMeta.setGMarkers(gMarkers);

        addCircle(gCircleMeta);
    }

    public Marker addMarker(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = super.addMarker(gMarkerMetadata);

        GMarker gMarker;
        switch (gMarkerMetadata.getType())
        {
            case GMarkerMetadata.ADDRESS_MARKER:
                gMarker = new AddressGMarker(context, marker, gMarkerMetadata);
                break;
            default:
                gMarker = new CommonGMarker(context, marker, gMarkerMetadata);
                break;
        }

        marker.setTag(gMarker);
        return marker;
    }

    @Override
    public Circle addCircle(@NonNull GCircleMeta gCircleMeta)
    {
        Circle circle = super.addCircle(gCircleMeta);

        GCircle gCircle = new CommonGCircle(context, circle, gCircleMeta);

        circle.setTag(gCircle);
        return circle;
    }

    @Nullable
    public List<GMarker> getLastSelectedCircleGMarkers()
    {
        if (lastSelectedCircle == null)
        {
            return null;
        }

        GCircle gCircle = (GCircle) lastSelectedCircle.getTag();
        return gCircle.getGCircleMeta().getGMarkers();
    }
}
